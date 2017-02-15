package yokohama.yellow_man.sena.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import play.mvc.Result;
import yokohama.yellow_man.sena.api.response.ApiResult;
import yokohama.yellow_man.sena.core.components.AppLogger;

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

		// パラメータマッピングバリデーションチェック
		Form<DataParams> requestParams = Form.form(DataParams.class).bindFromRequest();
		DataParams dataParams = requestParams.get();

		int draw = 1;
		if (dataParams.draw != null) {
			draw = dataParams.draw + 1;
		}

		for (Map.Entry<String, String> entry : dataParams.search.entrySet()) {
			AppLogger.info(entry.getKey() + ":" + entry.getValue());
		}

		for (Map<String, String> orderMap : dataParams.order) {
			for (Map.Entry<String, String> entry : orderMap.entrySet()) {
				AppLogger.info(entry.getKey() + ":" + entry.getValue());
			}
		}

		for (Map<String, String> columnsMap : dataParams.columns) {
			for (Map.Entry<String, String> entry : columnsMap.entrySet()) {
				AppLogger.info(entry.getKey() + ":" + entry.getValue());
			}
		}

		// データ
		Map<String, String> data = new HashMap<String, String>();
		data.put("first_name", "aaa");
		data.put("last_name", "bbb");
		data.put("position", "ccc");
		data.put("office", "ddd");
		data.put("start_date", "eee");
		data.put("salary", "fff");
		data.put("2first_name", "ggg");
		data.put("2last_name", "hhh");
		data.put("2position", "iii");
		data.put("2office", "jjj");
		data.put("2start_date", "kkk");
		data.put("2salary", "lll");
		data.put("2first_name", "mmm");
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		dataList.add(data);

		// レスポンスセット
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("draw", draw);
		retMap.put("recordsTotal", 1);
		retMap.put("recordsFiltered", 1);
		retMap.put("data", dataList);
		retMap.put("error", "");
		ret.setContent(retMap);

		return ok(ret.render());
	}

	public static class DataParams {
		public Integer draw;
		public Integer start;
		public Integer length;
		public Map<String, String> search;
		public List<Map<String, String>> order;
		public List<Map<String, String>> columns;
	}
}
