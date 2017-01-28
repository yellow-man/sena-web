package yokohama.yellow_man.sena.controllers;

import java.util.Date;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.components.db.StocksComponent;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.models.Stocks;
import yokohama.yellow_man.sena.pages.top.TopIndexPage;

/**
 * Topページコントローラ。
 *
 * @author yellow-man
 * @since 1.0
 */
public class TopController extends Controller {

	/**
	 * Topページアクション。
	 * @return
	 */
	public static Result index() {
		// ページ情報初期化
		TopIndexPage page = new TopIndexPage();

		// 「銘柄」テーブルより基準日として登録されてる「取得日」の最大値を取得する。
		Date date = StocksComponent.getMaxDateCache();
		AppLogger.info("銘柄「取得日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));

		// 「取得日」より、全「銘柄」を取得する。
		List<Stocks> stocksList = StocksComponent.getStocksListByDateCache(date);
		if (CheckUtils.isEmpty(stocksList)) {
			AppLogger.warn("銘柄リストが取得できませんでした。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		} else {
			AppLogger.info("銘柄リストが取得できました。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD)
							+ ", stocksList.size()=" + stocksList.size());

			page.stocks = stocksList;
		}

		return ok(views.html.top.index.render(page));
	}
}
