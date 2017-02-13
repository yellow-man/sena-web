package yokohama.yellow_man.sena.api.annotations.action;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import play.core.j.JavaResultExtractor;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.components.RequestUtilityComponent;

/**
 * リクエスト、レスポンスパラメータのトレース中継処理クラス。
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class ParamsTraceAction extends Action<ParamsTraceAction> {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		Request request = ctx.request();

		final String prefix = new StringBuffer("[" + RandomStringUtils.randomAlphanumeric(20) + "] ")
				.append("method=").append(request.method())
				.append(", url=").append(request.secure() ? "https://" : "http://").append(request.host()).append(request.uri())
				.toString();
		String startPrefix = "■■■start■■■" + prefix;

		Map<String, String[]> map = null;
		if ("GET".equals(request.method())) {
			map = RequestUtilityComponent.getQueryString(request);
		} else {
			map = RequestUtilityComponent.getFromPostStringMap(request);
		}

		StringBuffer buffRequest = new StringBuffer(startPrefix)
				.append("：").append(System.lineSeparator())
				.append("[REQUEST PARAMS]").append(System.lineSeparator());

		StringBuffer buffParams = new StringBuffer();
		if (!CheckUtils.isEmpty(map)) {
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				String key = entry.getKey();
				String[] valArr = entry.getValue();

				buffParams.append(key).append(":");
				StringBuffer buffValue = new StringBuffer();
				if (valArr != null) {
					for (String val : valArr) {
						buffValue.append(val).append(", ");
					}
				}
				if (buffValue.length() >= 2) {
					buffValue.delete(buffValue.length() - 2, buffValue.length());
				}
				buffParams.append(buffValue);
				buffParams.append(System.lineSeparator());
			}
		}
		if (buffParams.length() >= System.lineSeparator().length()) {
			buffParams.delete(buffParams.length() - System.lineSeparator().length(), buffParams.length());
		}

		buffRequest.append(buffParams).append(System.lineSeparator());
		AppLogger.info(buffRequest.toString());

		try {
			// メイン処理開始
			Promise<Result> promise = delegate.call(ctx);

			if (promise == null) {
				AppLogger.info(prefix + " レスポンスデータ取得：promise=null");
			} else {
				promise.map((result) -> {
					byte[] body = JavaResultExtractor.getBody(result, 0L);

					if (body == null) {
						AppLogger.info(prefix + " レスポンスデータ取得：body=null");
					} else {
						String strBody = new String(body, "UTF-8");

						try {
							JSONObject json = new JSONObject(strBody);
							if (json != null) {
								strBody = json.toString(2);
							}
						} catch (Exception e) {}

						String endPrefix = "■■■ end ■■■" + prefix;
						StringBuffer buffResponse = new StringBuffer(endPrefix)
								.append("：").append(System.lineSeparator())
								.append("[RESPONSE PARAMS]").append(System.lineSeparator())
								.append(strBody).append(System.lineSeparator());

						AppLogger.info(buffResponse.toString());
					}
					return result;
				});
			}

			return promise;
		} catch (Exception e) {
			throw e;
		}
	}
}
