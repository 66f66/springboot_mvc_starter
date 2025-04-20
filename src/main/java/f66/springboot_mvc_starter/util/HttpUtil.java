package f66.springboot_mvc_starter.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class HttpUtil {

    /**
     * @param url        파라미터가 포함된 전체 URL
     * @param paramName  파라미터 이름
     * @param paramValue 파라미터 값, 이미 파라미터의 값이 있다면 새로운 값으로 교체
     * @return 새로운 URL 반환
     */
    public static String setQueryParam(String url,
                                       String paramName,
                                       Object paramValue) {

        return UriComponentsBuilder.fromUriString(url)
                .replaceQueryParam(paramName, paramValue)
                .build()
                .toString();
    }

    /**
     * @param url    파라미터가 포함된 전체 URL
     * @param params 새로운 파라미터 map(파라미터 이름-파라미터 값), 기존의 파라미터가 있다면 교체
     * @return 새로운 URL 반환
     */
    public static String setQueryParams(String url,
                                        Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        params.forEach(builder::replaceQueryParam);

        return builder.build().toString();
    }

    /**
     * @param url       파라미터가 포함된 전체 URL
     * @param paramName 파라미터의 이름
     * @return 파라미터의 값(없으면 null, 배열이면 첫번째 값)
     */
    public static String getQueryParam(String url,
                                       String paramName) {

        UriComponents components = UriComponentsBuilder.fromUriString(url).build();

        List<String> values = components.getQueryParams().get(paramName);

        if (values == null || values.isEmpty()) {

            return null;
        }

        return values.getFirst();
    }

    /**
     * @param request 자바의 HTTP Request 객체
     * @return 요청이 ajax 거나 요청이나 받을 값의 컨텐츠 타입이 json 이라면 true 반환
     */
    public boolean isJsonRequest(HttpServletRequest request) {

        String requestedWithHeader = request.getHeader("X-Requested-With");
        String contentType = request.getHeader("Content-Type");
        String accept = request.getHeader("Accept");

        return "XMLHttpRequest".equalsIgnoreCase(requestedWithHeader) ||
                MediaType.APPLICATION_JSON_VALUE.equals(contentType) ||
                MediaType.APPLICATION_JSON_VALUE.equals(accept);
    }

    /**
     * @param request 자바의 HTTP Request 객체
     * @return 파라미터가 포함된 전체 URI
     */
    public String getRequestURIWithQuery(HttpServletRequest request) {

        return request.getRequestURI() +
                Optional.ofNullable(request.getQueryString()).map(qs -> "?" + qs).orElse("");
    }
}
