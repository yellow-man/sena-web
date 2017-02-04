package yokohama.yellow_man.sena.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import play.mvc.Result;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.components.db.DebitBalancesComponent;
import yokohama.yellow_man.sena.components.db.IndicatorsComponent;
import yokohama.yellow_man.sena.components.db.StocksComponent;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.models.DebitBalances;
import yokohama.yellow_man.sena.core.models.Indicators;
import yokohama.yellow_man.sena.core.models.Stocks;
import yokohama.yellow_man.sena.pages.top.TopIndexPage;

/**
 * Topページコントローラ。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 */
public class TopController extends AppWebController {

	/**
	 * Topページアクション。
	 * @return Result
	 * @since 1.0.0-1.0
	 */
	public static Result index() {
		// ページ情報初期化
		TopIndexPage page = new TopIndexPage();

		// 「銘柄」テーブルより基準日として、登録されている「取得日」の最大値を取得する。
		Date date = StocksComponent.getMaxDateCache();
		AppLogger.info("銘柄「取得日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		page.stocksDateStr = DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD);

		// 「取得日」より、全「銘柄」を取得する。
		List<Stocks> stocksList = StocksComponent.getStocksListByDateCache(date);
		if (CheckUtils.isEmpty(stocksList)) {
			AppLogger.warn("銘柄リストが取得できませんでした。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		} else {
			AppLogger.info("銘柄リストが取得できました。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD)
							+ ", stocksList.size()=" + stocksList.size());

			page.stocks = stocksList;
		}

		// 「指標」テーブルより基準日として、登録されている「取得日」の最大値を取得する。
		date = IndicatorsComponent.getMaxDateCache();
		AppLogger.info("指標「取得日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		page.indicatorsDateStr = DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD);

		// 「取得日」より、全「指標」を取得する。
		Map<Integer, Indicators> indicatorsMap = IndicatorsComponent.getIndicatorsMapByDateCache(date);
		if (CheckUtils.isEmpty(indicatorsMap)) {
			AppLogger.warn("指標マップが取得できませんでした。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		} else {
			AppLogger.info("指標マップが取得できました。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD)
							+ ", indicatorsMap.size()=" + indicatorsMap.size());

			page.indicatorsMap = indicatorsMap;
		}

		// 「信用残」テーブルより基準日として、登録されている「公開日」の最大値を取得する。
		date = DebitBalancesComponent.getMaxReleaseDateCache();
		AppLogger.info("信用残「公表日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		page.debitBalancesReleaseDateStr = DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD);

		// 「公表日」より、全「指標」を取得する。
		Map<Integer, DebitBalances> debitBalancesMap = DebitBalancesComponent.getDebitBalancesMapByDateCache(date);
		if (CheckUtils.isEmpty(debitBalancesMap)) {
			AppLogger.warn("信用残マップが取得できませんでした。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		} else {
			AppLogger.info("信用残マップが取得できました。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD)
							+ ", debitBalancesMap.size()=" + debitBalancesMap.size());

			page.debitBalancesMap = debitBalancesMap;
		}

		return ok(views.html.top.index.render(page));
	}
}
