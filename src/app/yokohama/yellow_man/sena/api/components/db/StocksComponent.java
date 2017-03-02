package yokohama.yellow_man.sena.api.components.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlRow;

import play.cache.Cache;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.api.params.DataTablesParams;
import yokohama.yellow_man.sena.core.components.AppLogger;
import yokohama.yellow_man.sena.core.components.ModelUtilityComponent;
import yokohama.yellow_man.sena.core.definitions.AppConsts;
import yokohama.yellow_man.sena.core.models.Stocks;

/**
 * 銘柄（stocks）モデルの操作を行うコンポーネントクラス。
 * <p>共通コンポーネント{@link yokohama.yellow_man.sena.components.db.StocksComponent}を拡張する。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 * @see yokohama.yellow_man.sena.components.db.StocksComponent
 */
public class StocksComponent extends yokohama.yellow_man.sena.components.db.StocksComponent {

	/**
	 * 検索条件に取得日（{@code date}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧総数を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @return 未削除の銘柄（stocks）情報一覧総数
	 * @since 1.1.0-1.1
	 */
	public static int getStocksTotalCountByDateCache(Date date) {
		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date;
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksTotalCountByDateCache" + ":" + date;

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (int) cache;
		}

		int count = getTotalCountByDate(date);

		// 取得データをキャッシュに保持
		Cache.set(cacheKey, count, AppConsts.CACHE_TIME_LONG);
		return count;
	}

	/**
	 * 検索条件に取得日（{@code date}）とDataTablesパラメータ（{@code params}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧総数を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @param params DataTablesパラメータ
	 * @return 未削除の銘柄（stocks）情報一覧フィルタアリング後総数
	 * @since 1.1.0-1.1
	 */
	public static int getStocksFilterCountByDateCache(Date date, DataTablesParams params) {
		// 検索文字列取得
		String searchValue = "";
		if (params.search != null && params.search.containsKey(DataTablesParams.MAP_KEY_SEARCH_VALUE)) {
			searchValue = params.search.get(DataTablesParams.MAP_KEY_SEARCH_VALUE);
		}

		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date + ":" + encryptStr(searchValue);
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksFilterCountByDateCache" + ":" + date + ":" + encryptStr(searchValue);

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (int) cache;
		}

		searchValue = "%" + searchValue + "%";

		String sql = "SELECT count(*) as count FROM stocks WHERE delete_flg = false AND date = :date AND (stock_code LIKE :searchValue OR stock_name LIKE :searchValue)";
		SqlRow sqlRow = Ebean.createSqlQuery(sql)
				.setParameter("date", date)
				.setParameter("searchValue", searchValue)
				.findUnique();

		int count = sqlRow.getInteger("count");

		// 取得データをキャッシュに保持
		Cache.set(cacheKey, count, AppConsts.CACHE_TIME_LONG);
		return count;
	}

	/**
	 * TODO yellow-man JOINしないと並び順は対応できない。
	 * 検索条件に取得日（{@code date}）とDataTablesパラメータ（{@code params}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @param params DataTablesパラメータ
	 * @return 未削除の銘柄（stocks）情報一覧
	 * @since 1.1.0-1.1
	 */
	@SuppressWarnings("unchecked")
	public static List<Stocks> getStocksListByDateCache(Date date, DataTablesParams params) {
		// 検索文字列取得
		String searchValue = "";
		if (params.search != null && params.search.containsKey(DataTablesParams.MAP_KEY_SEARCH_VALUE)) {
			searchValue = params.search.get(DataTablesParams.MAP_KEY_SEARCH_VALUE);
		}

		// 取得件数
		int limit = 10;
		if (params.length != null) {
			limit = params.length;
		}

		// ページ
		int page = 0;
		if (params.start != null) {
			page = params.start / limit;
		}

		// 並び順
		StringBuilder order = new StringBuilder();
		if (!CheckUtils.isEmpty(params.order)) {
			for (Map<String, String> orderMap : params.order) {
				// 必要なキーが存在しているかチェック
				if (orderMap.containsKey(DataTablesParams.MAP_KEY_ORDER_COLUMN) && orderMap.containsKey(DataTablesParams.MAP_KEY_ORDER_DIR)) {
					String columnStr = "";
					String dirStr = "";
					// column値チェック
					if (!DataTablesParams.ORDER_COLUMN_MAP.containsKey(orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN))) {
						AppLogger.warn("予期せぬ値が送られてきました。：order[column]=" + orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN));
						continue;
					} else {
						columnStr = DataTablesParams.ORDER_COLUMN_MAP.get(orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN));
					}
					// dir値チェック
					if (!DataTablesParams.ORDER_DIR_MAP.containsKey(orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR))) {
						AppLogger.warn("予期せぬ値が送られてきました。：order[dir]=" + orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR));
						continue;
					} else {
						dirStr = DataTablesParams.ORDER_DIR_MAP.get(orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR));
					}
					order.append(columnStr + " " + dirStr + ", ");
				}
			}
			if (order.length() > 0) {
				order.append("stock_code ASC");
			}
		}
		if (order.length() <= 0) {
			order.append("stock_code ASC");
		}

		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date + ":" + encryptStr(searchValue);
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksListByDateCache" + ":" + date + ":" + encryptStr(searchValue) + ":" + limit + ":" + page  + ":" + encryptStr(order.toString());

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (List<Stocks>) cache;
		}

		searchValue = "%" + searchValue + "%";

		List<Stocks> retList =
				Ebean.find(Stocks.class)
					.where()
					.eq("delete_flg", false)
					.eq("date", date)
					.disjunction()
						.like("stock_code", searchValue)
						.like("stock_name", searchValue)
					.endJunction()
					.orderBy(order.toString())
					.findPagingList(limit)
					.setFetchAhead(false)
					.getPage(page)
					.getList();

		// 取得データをキャッシュに保持
		if (retList != null) {
			Cache.set(cacheKey, retList, AppConsts.CACHE_TIME_LONG);
		}
		return retList;
	}

	/**
	 * TODO yellow-man JOINしないと並び順は対応できない。
	 * 検索条件に取得日（{@code date}）とDataTablesパラメータ（{@code params}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧を返す。（※キャッシュ：1時間）
	 *
	 * @param date 銘柄「取得日」の最大値
	 * @param indicatorsDate 指標「取得日」の最大値
	 * @param debitBalancesDate 信用残「公表日」の最大値
	 * @param params DataTablesパラメータ
	 * @return 未削除の銘柄（stocks）情報一覧
	 * @since 1.1.0-1.1
	 */
	@SuppressWarnings("unchecked")
	public static List<Stocks> getStocksWithListByDateCache(
			Date date, Date indicatorsDate, Date debitBalancesDate, DataTablesParams params) {

		// 検索文字列取得
		String searchValue = "";
		if (params.search != null && params.search.containsKey(DataTablesParams.MAP_KEY_SEARCH_VALUE)) {
			searchValue = params.search.get(DataTablesParams.MAP_KEY_SEARCH_VALUE);
		}

		// 取得件数
		int limit = 10;
		if (params.length != null) {
			limit = params.length;
		}

		// ページ
		int page = 0;
		if (params.start != null) {
			page = params.start / limit;
		}

		// 並び順
		StringBuilder order = new StringBuilder();
		if (!CheckUtils.isEmpty(params.order)) {
			for (Map<String, String> orderMap : params.order) {
				// 必要なキーが存在しているかチェック
				if (orderMap.containsKey(DataTablesParams.MAP_KEY_ORDER_COLUMN) && orderMap.containsKey(DataTablesParams.MAP_KEY_ORDER_DIR)) {
					String columnStr = "";
					String dirStr = "";
					// column値チェック
					if (!DataTablesParams.ORDER_COLUMN_MAP.containsKey(orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN))) {
						AppLogger.warn("予期せぬ値が送られてきました。：order[column]=" + orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN));
						continue;
					} else {
						columnStr = DataTablesParams.ORDER_COLUMN_MAP.get(orderMap.get(DataTablesParams.MAP_KEY_ORDER_COLUMN));
					}
					// dir値チェック
					if (!DataTablesParams.ORDER_DIR_MAP.containsKey(orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR))) {
						AppLogger.warn("予期せぬ値が送られてきました。：order[dir]=" + orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR));
						continue;
					} else {
						dirStr = DataTablesParams.ORDER_DIR_MAP.get(orderMap.get(DataTablesParams.MAP_KEY_ORDER_DIR));
					}
					order.append(columnStr + " " + dirStr + ", ");
				}
			}
			if (order.length() > 0) {
				order.append("stocks.stock_code ASC");
			}
		}
		if (order.length() <= 0) {
			order.append("stocks.stock_code ASC");
		}

		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date + ":" + encryptStr(searchValue);
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksListByDateCache" + ":" + date + ":" + encryptStr(searchValue) + ":" + limit + ":" + page  + ":" + encryptStr(order.toString());

		Object cache = null;
//		if ((cache = Cache.get(cacheKey)) != null) {
//			// キャッシュが存在する場合は、キャッシュからデータを取得する。
//			return (List<StocksWithIndicatorsDebitBalances>) cache;
//		}

		searchValue = "%" + searchValue + "%";

		// 銘柄クラスのカラムMapを取得する
		Map<String, String> stocksColumnMap = ModelUtilityComponent.getColumnMap(Stocks.class);

		// SQLを組み立てる
		StringBuilder sql = new StringBuilder(" SELECT ");
		StringBuilder select = new StringBuilder();
		String prefix = "stocks.";
		for (Map.Entry<String, String> entry : stocksColumnMap.entrySet()) {
			select.append(", ").append(prefix).append(entry.getKey());
		}
		select.deleteCharAt(0);

		sql.append(select);
		sql.append(" FROM ");
		sql.append("     stocks ");
		sql.append("         LEFT JOIN indicators indicators ON ");
		sql.append("             indicators.stock_code = stocks.stock_code ");
		sql.append("             AND indicators.date = '" + DateUtils.toString(indicatorsDate, DateUtils.DATE_FORMAT_YYYY_MM_DD) + "' ");
		sql.append("         LEFT JOIN debit_balances debitBalances ON ");
		sql.append("             debitBalances.stock_code = stocks.stock_code ");
		sql.append("             AND debitBalances.release_date = '" + DateUtils.toString(debitBalancesDate, DateUtils.DATE_FORMAT_YYYY_MM_DD) + "' ");
		sql.append(" WHERE ");
		sql.append("     stocks.date = '" + DateUtils.toString(date, DateUtils.DATE_FORMAT_YYYY_MM_DD) + "' ");
		sql.append("     AND (stocks.stock_code like ? OR stocks.stock_name like ?) ");
		sql.append(" ORDER BY ");
		sql.append(order);
		sql.append(" LIMIT ? ");
		sql.append(" OFFSET ? ");

		// RawSqlBuilderを組み立てる
		RawSqlBuilder rawSqlBuilder = RawSqlBuilder.unparsed(sql.toString());
		prefix = "stocks.";
		for (Map.Entry<String, String> entry : stocksColumnMap.entrySet()) {
			rawSqlBuilder.columnMapping(prefix + entry.getKey(), entry.getValue());
		}

		RawSql rawSql = rawSqlBuilder.create();
		List<Stocks> retList =
				Ebean.find(Stocks.class)
					.setRawSql(rawSql)
					.setParameter(1, searchValue)
					.setParameter(2, searchValue)
					.setParameter(3, limit)
					.setParameter(4, page * limit)
					.findList();

		// 取得データをキャッシュに保持
		if (retList != null) {
			Cache.set(cacheKey, retList, AppConsts.CACHE_TIME_LONG);
		}
		return retList;
	}

	/**
	 * TODO yellow-man とりあえず一旦privateメソッドで。common-tools 移動予定。
	* @param text ハッシュ化するテキスト。
	* @return ハッシュ化した計算値(16進数)。
	*/
	private static String encryptStr(String text) {
		// 変数初期化
		MessageDigest md = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// メッセージダイジェストインスタンス取得
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// 例外発生時、エラーメッセージ出力
			System.out.println("指定された暗号化アルゴリズムがありません。");
		}

		// メッセージダイジェスト更新
		md.update(text.getBytes());

		// ハッシュ値を格納
		byte[] valueArray = md.digest();

		// ハッシュ値の配列をループ
		for(int i = 0; i < valueArray.length; i++){
			// 値の符号を反転させ、16進数に変換
			String tmpStr = Integer.toHexString(valueArray[i] & 0xff);

			if(tmpStr.length() == 1){
				// 値が一桁だった場合、先頭に0を追加し、バッファに追加
				buffer.append('0').append(tmpStr);
			} else {
				// その他の場合、バッファに追加
				buffer.append(tmpStr);
			}
		}

		// 完了したハッシュ計算値を返却
		return buffer.toString();
	}
}
