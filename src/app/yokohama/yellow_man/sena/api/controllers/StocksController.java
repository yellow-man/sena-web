package yokohama.yellow_man.sena.api.controllers;

import play.mvc.Result;
import yokohama.yellow_man.sena.api.response.ApiResult;

/**
 * 銘柄APIコントローラークラス。
 * <p>一覧取得の機能を提供する。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class StocksController extends AppWebApiController {

	public static Result getStocksWithInfoList() {
		// 返却値初期化
		ApiResult ret = new ApiResult(API_RES_SUCCESS);
		return ok(ret.render());
	}
}
