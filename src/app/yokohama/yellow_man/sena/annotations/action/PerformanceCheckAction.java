package yokohama.yellow_man.sena.annotations.action;

import java.util.Date;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import yokohama.yellow_man.sena.core.components.AppLogger;

/**
 * パフォーマンスチェック 中継処理クラス。
 * <p>アクション実行前と実行後の時間をログに出力する中継処理
 * @author yellow-man
 * @since 1.1
 */
public class PerformanceCheckAction extends Action<PerformanceCheckAction> {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		Date prefixDate = new Date();
		AppLogger.info(new StringBuffer(ctx.toString())
				.append(" start：")
				.append(PerformanceCheckAction.class.getSimpleName())
				.append("_")
				.append(prefixDate.getTime())
				.toString());

		// メイン処理開始
		Promise<Result> promise = delegate.call(ctx);

		Date endDate = new Date();
		AppLogger.info(new StringBuffer(ctx.toString())
				.append("  end ：")
				.append(PerformanceCheckAction.class.getSimpleName())
				.append("_")
				.append(prefixDate.getTime())
				.append("(経過時間：")
				.append((endDate.getTime() - prefixDate.getTime()))
				.append("ms)")
				.toString());
		return promise;
	}
}
