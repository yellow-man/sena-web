package yokohama.yellow_man.sena.controllers;

import java.util.Date;

import play.mvc.Result;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.components.db.DebitBalancesComponent;
import yokohama.yellow_man.sena.components.db.IndicatorsComponent;
import yokohama.yellow_man.sena.components.db.StocksComponent;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.pages.top.TopIndexPage;

/**
 * Topページコントローラ。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 * @version 1.1.0-1.1
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

		// 「指標」テーブルより基準日として、登録されている「取得日」の最大値を取得する。
		date = IndicatorsComponent.getMaxDateCache();
		AppLogger.info("指標「取得日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		page.indicatorsDateStr = DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD);

		// 「信用残」テーブルより基準日として、登録されている「公開日」の最大値を取得する。
		date = DebitBalancesComponent.getMaxReleaseDateCache();
		AppLogger.info("信用残「公表日」取得。：date=" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		page.debitBalancesReleaseDateStr = DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD);

		return ok(views.html.top.index.render(page));
	}
}
