package yokohama.yellow_man.sena.annotations.action;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.components.RequestUtilityComponent;

/**
 * リクエスト、レスポンスパラメータのトレース中継処理クラス。
 * @author yellow-man
 * @since 1.0.0-1.0
 */
public class ParamsTraceAction extends Action<ParamsTraceAction> {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		final String prefix = new StringBuffer()
				.append("[").append(RandomStringUtils.randomAlphanumeric(10)).append("]")
				.toString();

		StringBuffer buffRequest = new StringBuffer("■■start■■").append(prefix).append("：[REQUEST PARAMS]");

		Map<String, String[]> headersMap = RequestUtilityComponent.getFromPostMap(ctx.request());
		if (!CheckUtils.isEmpty(headersMap)) {
			StringBuffer buffParams = new StringBuffer();

			for (Map.Entry<String, String[]> entry : headersMap.entrySet()) {
				String key = entry.getKey();
				String[] values = entry.getValue();

				buffParams.append(System.lineSeparator()).append(key).append(":");
				if (values != null) {
					StringBuffer buffValues = new StringBuffer();
					for (String val : values) {
						buffValues.append(val).append(", ");
					}
					if (buffValues.length() >= 2) {
						buffValues.delete(buffValues.length() - 2, buffValues.length());
					}
					buffParams.append(buffValues);
				}
			}
			buffRequest.append(buffParams).append(System.lineSeparator());
		}
		AppLogger.info(buffRequest.toString());

		Promise<Result> promise = delegate.call(ctx);

		String endPrefix = "■■ end ■■" + prefix;
		StringBuffer buffResponse = new StringBuffer(endPrefix).append("：[RESPONSE PARAMS]");
		AppLogger.info(buffResponse.toString());
		return promise;
	}
}
