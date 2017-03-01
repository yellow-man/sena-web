package yokohama.yellow_man.sena.api.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import play.mvc.Result;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.api.components.db.DebitBalancesComponent;
import yokohama.yellow_man.sena.api.components.db.IndicatorsComponent;
import yokohama.yellow_man.sena.api.components.db.StocksComponent;
import yokohama.yellow_man.sena.api.params.DataTablesParams;
import yokohama.yellow_man.sena.api.response.ApiResult;
import yokohama.yellow_man.sena.api.response.StocksWithInfo;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.models.ext.StocksWithIndicatorsDebitBalances;
import yokohama.yellow_man.sena.views.helper.AppHelper;

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
	 *   "result": 0,
	 *   "draw": 1,
	 *   "recordsTotal": 10,
	 *   "recordsFiltered": 10,
	 *   "data": [
	 *     {
	 *       "stockCodeName": "9983 (株)ファーストリテイリング",
	 *       "market": "東証1部",
	 *       "dividendYield": "0.97 %",
	 *       "priceEarningsRatio": "36.70 倍",
	 *       "priceBookValueRatio": "5.17 倍",
	 *       "earningsPerShare": "980.71",
	 *       "bookValuePerShare": "6,967.28",
	 *       "returnOnEquity": "14.09",
	 *       "capitalRatio": "46.40",
	 *       "marginSellingBalance": "202,400",
	 *       "marginDebtBalance": "545,000",
	 *       "ratioMarginBalance": "2.69 倍"
	 *     }
	 *   ]
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

		// 描画カウンターを取得・セット
		int draw = 1;
		if (dataParams.draw != null) {
			draw = dataParams.draw + 1;
		}

		// 「銘柄」テーブルより基準日として、登録されている「取得日」の最大値を取得する。
		Date date = StocksComponent.getMaxDateCache();
		AppLogger.info("銘柄「取得日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));

		// 表示対象の総数を取得する。
		int recordsTotal = StocksComponent.getStocksTotalCountByDateCache(date);
		AppLogger.info("表示対象の総数取得。：recordsTotal=" + recordsTotal);

		// フィルタリング後の表示対象総数を取得する。
		Integer recordsFiltered = StocksComponent.getStocksFilterCountByDateCache(date, dataParams);
		AppLogger.info("フィルタリング後の表示対象総数取得。：recordsFiltered=" + recordsFiltered);

		// 「指標」テーブルより基準日として、登録されている「取得日」の最大値を取得する。
		Date indicatorsDate = IndicatorsComponent.getMaxDateCache();
		AppLogger.info("指標「取得日」取得。：date=" + DateUtils.toString(indicatorsDate, DateUtils.DATE_FORMAT_YYYY_MM_DD));

		// 「信用残」テーブルより基準日として、登録されている「公表日」の最大値を取得する。
		Date debitBalancesDate = DebitBalancesComponent.getMaxReleaseDateCache();
		AppLogger.info("信用残「公表日」取得。：date=" + DateUtils.toString(debitBalancesDate, DateUtils.DATE_FORMAT_YYYY_MM_DD));

		// 返却データ。
		List<StocksWithInfo> dataList = null;

		// 「銘柄」データを取得する。
		List<StocksWithIndicatorsDebitBalances> stocksList = StocksComponent.getStocksWithListByDateCache(date, indicatorsDate, debitBalancesDate, dataParams);
		if (CheckUtils.isEmpty(stocksList)) {
			AppLogger.warn("銘柄リストが取得できませんでした。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		} else {
			int stocksListSize = stocksList.size();
			AppLogger.info("銘柄リストが取得できました。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD)
							+ ", stocksListSize=" + stocksListSize);

			// 返却データ初期化。
			dataList = new ArrayList<StocksWithInfo>();
			// 返却データに詰める。
			for (StocksWithIndicatorsDebitBalances stocks : stocksList) {
				StocksWithInfo data = new StocksWithInfo();
				Integer stockCode = stocks.stockCode;
				// 銘柄（コード）
				data.stockCodeName        = stockCode + " " + stocks.stockName;
				// 市場名
				data.market               = stocks.market;

				// 該当の「指標」データを取得
				if (stocks.indicators != null) {
					// 配当利回り
					data.dividendYield        = (stocks.indicators.dividendYield != null)       ? stocks.indicators.dividendYield.toString() + " %"        : "";
					// 株価収益率（PER）
					data.priceEarningsRatio   = (stocks.indicators.priceEarningsRatio != null)  ? stocks.indicators.priceEarningsRatio.toString() + " 倍"  : "";
					// 株価純資産倍率（PBR）
					data.priceBookValueRatio  = (stocks.indicators.priceBookValueRatio != null) ? stocks.indicators.priceBookValueRatio.toString() + " 倍" : "";
					// 1株利益（EPS）
					data.earningsPerShare     = (stocks.indicators.earningsPerShare != null)    ? AppHelper.format(stocks.indicators.earningsPerShare,  AppHelper.NUM_FORMAT_ASIGN_COMMA_POINT) : "";
					// 1株当たり純資産（BPS）
					data.bookValuePerShare    = (stocks.indicators.bookValuePerShare != null)   ? AppHelper.format(stocks.indicators.bookValuePerShare, AppHelper.NUM_FORMAT_ASIGN_COMMA_POINT) : "";
					// 株主資本利益率（ROE）
					data.returnOnEquity       = (stocks.indicators.returnOnEquity != null)      ? AppHelper.format(stocks.indicators.returnOnEquity,    AppHelper.NUM_FORMAT_ASIGN_COMMA_POINT) : "";
					// 自己資本比率
					data.capitalRatio         = (stocks.indicators.capitalRatio != null)        ? stocks.indicators.capitalRatio.toString() + " %"         : "";
				}

				// 該当の「信用残」データを取得
				if (stocks.debitBalances != null) {
					// 信用売残
					data.marginSellingBalance = (stocks.debitBalances.marginSellingBalance != null) ? AppHelper.format(stocks.debitBalances.marginSellingBalance, AppHelper.NUM_FORMAT_ASIGN_COMMA)       : "";
					// 信用買残
					data.marginDebtBalance    = (stocks.debitBalances.marginDebtBalance != null)    ? AppHelper.format(stocks.debitBalances.marginDebtBalance,    AppHelper.NUM_FORMAT_ASIGN_COMMA)       : "";
					// 信用倍率
					data.ratioMarginBalance   = (stocks.debitBalances.ratioMarginBalance != null)   ? AppHelper.format(stocks.debitBalances.ratioMarginBalance,   AppHelper.NUM_FORMAT_ASIGN_COMMA_POINT) : "";
				}

				dataList.add(data);
			}
		}

		// レスポンスセット
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 描画カウンタ
		retMap.put("draw", draw);
		// フィルタリング前の合計レコード（データベース内のレコードの総数）
		retMap.put("recordsTotal", recordsTotal);
		// フィルタリング後の合計レコード（フィルタリングが適用された後のレコードの総数）
		retMap.put("recordsFiltered", recordsFiltered);
		// 表に表示されるデータ。
		retMap.put("data", dataList);
		// エラーメッセージ（※エラーが発生していない場合は無し。）
		//retMap.put("error", "");
		ret.setContent(retMap);

		return ok(ret.render());
	}
}
