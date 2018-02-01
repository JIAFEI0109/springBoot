package com.cqabj.springboot.web.config.interceptor;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.common.ResultInfo;
import com.cqabj.springboot.model.entity.AccessKeyInfo;
import com.cqabj.springboot.utils.HmacSHA1;
import com.cqabj.springboot.utils.NetworkUtil;
import com.cqabj.springboot.web.common.props.NoSignSecurityProperties;
import com.cqabj.springboot.web.service.AccessKeyService;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 签名拦击器（servlet拦截器）
 * 拦截所有为标注的不拦截的接口,在controller执行之前进行拦截
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
public class SignInterceptor implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        boolean ifDoChain = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = String.valueOf(request.getRequestURL());
        NoSignSecurityProperties bean = getBean(NoSignSecurityProperties.class, request);
        //匹配免过滤路径
        for (String unFiltUrl : bean.getResources()) {
            if (containsUrl(url, bean.getContextPath()[0] + unFiltUrl)) {
                ifDoChain = true;
                break;
            }
        }
        //鉴权
        if (ifDoChain || signInterceptor(request, response)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    private boolean signInterceptor(HttpServletRequest request, HttpServletResponse response) {
        //客户端数字签名密文
        String clientSign;
        AccessKeyInfo accessKeyInfo;
        //系统时间:毫秒数/1000
        long sysTime = System.currentTimeMillis() / IGlobalConstant.SECONDS_TO_MILLISECONDS;
        //请求类型
        String method = request.getMethod();
        //请求数字签名(格式: accessKeyId:clinetSign)
        String authorization = request.getHeader("Authorization");
        //请求时间
        String signDate = request.getHeader("Sign-Date");
        //数据格式
        String contentType = request.getHeader("Content-Type");
        //GUID(消息唯一标识)
        String msgIdent = request.getHeader("Msg-Ident");
        //接口调用人员ID
        Long operatorId = -1L;
        String operator = request.getHeader("Operator");
        if (operator != null) {
            operatorId = Long.valueOf(operator);
        }
        //缓存
        //TODO 暂时未写缓存
        //判断签名验证是否超时(5分钟)
        if (!StringUtils.isEmpty(signDate) && Range.lessThan(IGlobalConstant.SIGN_LIVE_TIME)
            .contains(sysTime - Long.valueOf(signDate))) {
            NetworkUtil.responseJSONMsg(response, ResultInfo.createResult(CodeEnum.ERROR_10004));
            return false;
        }
        if (StringUtils.isEmpty(authorization)) {
            NetworkUtil.responseJSONMsg(response, ResultInfo.createResult(CodeEnum.ERROR_10004));
            return false;
        }
        List<String> authorizations = Splitter.on(IGlobalConstant.COLON).splitToList(authorization);
        clientSign = authorizations.get(1);
        AccessKeyService accessKeyService = getBean(AccessKeyService.class, request);
        accessKeyInfo = accessKeyService.getAccessKeyInfoById(authorizations.get(0));
        //若密钥为空,无权访问
        if (accessKeyInfo == null) {
            NetworkUtil.responseJSONMsg(response, ResultInfo.createResult(CodeEnum.ERROR_10004));
            return false;
        }
        //判断证书是否为失效
        if (Range.lessThan(sysTime).contains(
            accessKeyInfo.getFailureTime().getTime() / IGlobalConstant.SECONDS_TO_MILLISECONDS)) {
            NetworkUtil.responseJSONMsg(response, ResultInfo.createResult(CodeEnum.ERROR_10005));
            return false;
        }
        //签名算法
        String hmascSha1 = method + "\n" + contentType + "\n" + signDate + "\n" + msgIdent;
        //系统根据签名算法算出的密文与传入密文进行对比校验
        byte[] hmascSha1Bytes = HmacSHA1.hamcsha1(hmascSha1.getBytes(),
            accessKeyInfo.getAccessKeySecret().getBytes());
        if (hmascSha1Bytes.length == 0) {
            return false;
        }
        byte[] encodeStr = Base64.encode(hmascSha1Bytes);
        String sysBySign = new String(encodeStr);
        return sysBySign.equals(clientSign);

    }

    @Override
    public void destroy() {
        //Auto-generated method stub
    }

    private <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        return WebApplicationContextUtils
            .getRequiredWebApplicationContext(request.getServletContext()).getBean(clazz);
    }

    private boolean containsUrl(String url, String targetUrl) {
        //根节点匹配规则
        if (StringUtils.contains(targetUrl, IGlobalConstant.ALL_EXEMPTED)) {
            String subTarget = CharMatcher.anyOf(IGlobalConstant.ALL_EXEMPTED)
                .removeFrom(targetUrl);
            if (subTarget.length() > url.length()) {
                return false;
            }
            if (subTarget.length() < url.length()
                && url.charAt(subTarget.length()) != IGlobalConstant.FORWARD_SLASH) {
                return false;
            }
            String subUrl = StringUtils.substring(url, IGlobalConstant.IO_READ_START_TAG,
                subTarget.length());

            return subUrl.equals(subTarget);

        }
        return targetUrl.equals(url);
    }
}
