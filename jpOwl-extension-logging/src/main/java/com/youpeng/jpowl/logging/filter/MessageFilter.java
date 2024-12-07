import com.youpeng.jpowl.logging.filter.LogFilter;
import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.regex.Pattern;

/**
 * 消息内容过滤器
 * 支持正则表达式匹配
 */
public class MessageFilter implements LogFilter {
    private final Pattern pattern;
    
    public MessageFilter(String regex) {
        this.pattern = Pattern.compile(regex);
    }
    
    @Override
    public boolean accept(LogEvent event) {
        return pattern.matcher(event.getMessage()).matches();
    }
    
    @Override
    public LogEvent process(LogEvent event) {
        return event;
    }
} 