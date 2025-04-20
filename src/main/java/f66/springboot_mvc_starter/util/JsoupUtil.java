package f66.springboot_mvc_starter.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class JsoupUtil {

    public String sanitizeHtml(String html) {
        return Jsoup.clean(html, Safelist.relaxed()
                .addTags("iframe")
                .addAttributes("iframe", "src", "frameborder", "allowfullscreen")
        );
    }
}
