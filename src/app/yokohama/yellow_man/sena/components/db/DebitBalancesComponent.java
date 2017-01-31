package yokohama.yellow_man.sena.components.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import play.cache.Cache;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.ClassUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.sena.core.definitions.AppConsts;
import yokohama.yellow_man.sena.core.models.DebitBalances;

/**
 * 信用残（debit_balances）モデルの操作を行うコンポーネントクラス。
 * <p>共通コンポーネント{@link yokohama.yellow_man.sena.core.components.db.DebitBalancesComponent}を拡張する。
 *
 * @author yellow-man
 * @since 1.0
 * @see yokohama.yellow_man.sena.core.components.db.DebitBalancesComponent
 */
public class DebitBalancesComponent extends yokohama.yellow_man.sena.core.components.db.DebitBalancesComponent {

	/**
	 * 信用残（debit_balances）情報公開日の最大値を返す。（※キャッシュ：1時間）
	 *
	 * @return 公開日の最大値
	 * @since 1.0
	 */
	public static Date getMaxReleaseDateCache() {
		// キャッシュキー
		String cacheKey = DebitBalancesComponent.class.getName() + ":" + ClassUtils.getMethodName();

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (Date) cache;
		}

		List<DebitBalances> retList =
				Ebean.find(DebitBalances.class)
					.where()
					.eq("delete_flg", false)
					.orderBy("id DESC")
					.findPagingList(1)
					.setFetchAhead(false)
					.getPage(0)
					.getList();

		Date releaseDate = null;
		if (!CheckUtils.isEmpty(retList)) {
			releaseDate = retList.get(0).releaseDate;
		} else {
			releaseDate = DateUtils.getJustDate(new Date());
		}

		// 取得データをキャッシュに保持
		if (releaseDate != null) {
			Cache.set(cacheKey, releaseDate, AppConsts.CACHE_TIME_LONG);
		}
		return releaseDate;
	}

	/**
	 * 検索条件に公表日（{@code release_date}）を指定し、
	 * 未削除の信用残（debit_balances）情報一覧をマップで返す。（※キャッシュ：1時間）
	 *
	 * @param releaseDate 公表日
	 * @return 未削除の信用残（debit_balances）情報一覧 Map<銘柄コード, 信用残>
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, DebitBalances> getDebitBalancesMapByDateCache(Date releaseDate) {
		// キャッシュキー
		String cacheKey = DebitBalancesComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + releaseDate;

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (Map<Integer, DebitBalances>) cache;
		}

		Map<Integer, DebitBalances> retMap =
				yokohama.yellow_man.sena.core.components.db.DebitBalancesComponent.getDebitBalancesMapByDate(releaseDate);

		// 取得データをキャッシュに保持
		if (retMap != null) {
			Cache.set(cacheKey, retMap, AppConsts.CACHE_TIME_LONG);
		}
		return retMap;
	}
}
