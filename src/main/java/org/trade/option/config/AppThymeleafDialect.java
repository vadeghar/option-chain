package org.trade.option.config;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Adds new expressions to the default Thymeleaf dialect. This allows services 
 * to be called as if they are part of Thymeleaf, using prefix "#".
 * <pre>
 * th:text="${#myService.formatMyText('TextToFormat')}"
 * </pre>
 */
public class AppThymeleafDialect extends AbstractDialect implements IExpressionObjectDialect {
	
	protected AppThymeleafDialect() {
		super("Thymeleaf Dialect");
	}

//	@Autowired
//	private BuildProperties buildProperties;

	@Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
        	private Map<String,Object> expressionObjects = new HashMap<>();
        	
            @Override
            public Set<String> getAllExpressionObjectNames() {
//            	expressionObjects.put("build", buildProperties);
                return expressionObjects.keySet();
            }

            @Override
            public Object buildObject(IExpressionContext context,
                    String expressionObjectName) {
            	return expressionObjects.get(expressionObjectName);
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }

}