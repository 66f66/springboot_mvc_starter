package f66.springboot_mvc_starter.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkdownUtil {

    /**
     * @param markdown 마크다운 문법으로 작성된 문자열
     * @return html 변환된 문자열로 반환
     */
    public static String markdownToHtml(String markdown) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }
}
