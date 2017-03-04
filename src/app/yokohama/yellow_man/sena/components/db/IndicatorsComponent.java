package yokohama.yellow_man.sena.components.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import play.cache.Cache;
import yokohama.yellow_man.common_tools.CheckUtils;
import yokohama.yellow_man.common_tools.ClassUtils;
import yokohama.yellow_man.common_tools.DateUtils;
import yokohama.yellow_man.common_tools.StringUtils;
import yokohama.yellow_man.sena.core.definitions.AppConsts;
import yokohama.yellow_man.sena.core.models.Indicators;

/**
 * 指標（indicators）モデルの操作を行うコンポーネントクラス。
 * <p>共通コンポーネント{@link yokohama.yellow_man.sena.core.components.db.IndicatorsComponent}を拡張する。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 * @version 1.1.0-1.1
 * @see yokohama.yellow_man.sena.core.components.db.IndicatorsComponent
 */
public class IndicatorsComponent extends yokohama.yellow_man.sena.core.components.db.IndicatorsComponent {

	/**
	 * 指標（indicators）情報取得日の最大値を返す。（※キャッシュ：1時間）
	 *
	 * @return 取得日の最大値
	 * @since 1.0.0-1.0
	 */
	public static Date getMaxDateCache() {
		// キャッシュキー
		String cacheKey = IndicatorsComponent.class.getName() + ":" + ClassUtils.getMethodName();

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (Date) cache;
		}

		List<Indicators> retList =
				Ebean.find(Indicators.class)
					.where()
					.eq("delete_flg", false)
					.orderBy("id DESC")
					.findPagingList(1)
					.setFetchAhead(false)
					.getPage(0)
					.getList();

		Date date = null;
		if (!CheckUtils.isEmpty(retList)) {
			date = retList.get(0).date;
		} else {
			date = DateUtils.getJustDate(new Date());
		}

		// 取得データをキャッシュに保持
		if (date != null) {
			Cache.set(cacheKey, date, AppConsts.CACHE_TIME_LONG);
		}
		return date;
	}

	/**
	 * 検索条件に取得日（{@code date}）を指定し、
	 * 未削除の指標（indicators）情報一覧をマップで返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @return 未削除の指標（indicators）情報一覧 Map＜銘柄コード, 指標＞
	 * @since 1.0.0-1.0
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Indicators> getIndicatorsMapByDateCache(Date date) {
		// キャッシュキー
		String cacheKey = IndicatorsComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + StringUtils.encryptStr(date.toString());

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (Map<Integer, Indicators>) cache;
		}

		Map<Integer, Indicators> retMap =
				yokohama.yellow_man.sena.core.components.db.IndicatorsComponent.getIndicatorsMapByDate(date);

		// 取得データをキャッシュに保持
		if (retMap != null) {
			Cache.set(cacheKey, retMap, AppConsts.CACHE_TIME_LONG);
		}
		return retMap;
	}
}
