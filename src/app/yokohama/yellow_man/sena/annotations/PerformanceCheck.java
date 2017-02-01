package yokohama.yellow_man.sena.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;
import yokohama.yellow_man.sena.annotations.action.PerformanceCheckAction;

/**
 * パフォーマンスチェック中継処理 アノテーション定義。
 * @author yellow-man
 * @since 1.1
 * @see UsersTraceAction
 */
@With(PerformanceCheckAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceCheck {}
