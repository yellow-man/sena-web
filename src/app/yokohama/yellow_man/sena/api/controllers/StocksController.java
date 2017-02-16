package yokohama.yellow_man.sena.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import play.mvc.Result;
import yokohama.yellow_man.sena.api.params.DataTablesParams;
import yokohama.yellow_man.sena.api.response.ApiResult;
import yokohama.yellow_man.sena.api.response.StocksWithInfo;

/**
 * 銘柄APIコントローラークラス。
 * <p>一覧取得の機能を提供する。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class StocksController extends AppWebApiController {

	/**
	 * 銘柄情報と銘柄情報に付属する指標、信用残を取得する。
	 * @return json文字列
	 * <pre>
	 * {
	 *   "result": 0
	 * }
	 * </pre>
	 * @since 1.1.0-1.1
	 */
	public static Result getStocksWithInfoList() {
		// 返却値初期化
		ApiResult ret = new ApiResult(API_RES_SUCCESS);

		// パラメータマッピングバリデーションチェック
		Form<DataTablesParams> requestParams = Form.form(DataTablesParams.class).bindFromRequest();
		DataTablesParams dataParams = requestParams.get();

		// バリデーションチェック結果
		if (requestParams.hasErrors()) {
			ret.setErrors(requestParams.errorsAsJson(), "エラー");
			ret.setResult(API_RES_FAILURE);
			return ok(ret.render());
		}

		int draw = 1;
		if (dataParams.draw != null) {
			draw = dataParams.draw + 1;
		}

		// データ
		StocksWithInfo data = new StocksWithInfo();
		data.stockCodeName        = "aaa";
		data.market               = "bbb";

		data.dividendYield        = "ccc";
		data.priceEarningsRatio   = "ddd";
		data.priceBookValueRatio  = "eee";
		data.earningsPerShare     = "fff";
		data.bookValuePerShare    = "ggg";
		data.returnOnEquity       = "hhh";
		data.capitalRatio         = "iii";

		data.marginSellingBalance = "jjj";
		data.marginDebtBalance    = "kkk";
		data.ratioMarginBalance   = "lll";
		List<StocksWithInfo> dataList = new ArrayList<StocksWithInfo>();
		dataList.add(data);

		// レスポンスセット
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("draw", draw);
		retMap.put("recordsTotal", 1);
		retMap.put("recordsFiltered", 1);
		retMap.put("data", dataList);
		retMap.put("error", "");
		ret.setContent(retMap);

		return ok(ret.render());
	}
}
