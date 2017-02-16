package yokohama.yellow_man.sena.api.response;

/**
 * 銘柄情報と銘柄情報に付属する指標、信用残を保持するクラス。
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class StocksWithInfo {
	/** 銘柄情報：銘柄コード 銘柄名 */
	public String stockCodeName;
	/** 銘柄情報：市場名 */
	public String market;

	/** 指標情報：配当利回り（整数部：8桁、小数部：2桁） */
	public String dividendYield;
	/** 指標情報：株価収益率（PER、整数部：8桁、小数部：2桁） */
	public String priceEarningsRatio;
	/** 指標情報：株価純資産倍率（PBR、整数部：8桁、小数部：2桁） */
	public String priceBookValueRatio;
	/** 指標情報：1株利益（EPS、整数部：8桁、小数部：2桁） */
	public String earningsPerShare;
	/** 指標情報：1株当たり純資産（BPS、整数部：8桁、小数部：2桁） */
	public String bookValuePerShare;
	/** 指標情報：株主資本利益率（ROE、整数部：5桁、小数部：20桁） */
	public String returnOnEquity;
	/** 指標情報：自己資本比率（整数部：8桁、小数部：2桁） */
	public String capitalRatio;

	/** 信用情報：信用売残（ハイフン等、数値に変換できない場合：-1） */
	public String marginSellingBalance;
	/** 信用情報：信用買残（ハイフン等、数値に変換できない場合：-1） */
	public String marginDebtBalance;
	/** 信用情報：信用倍率（整数部：8桁、小数部：2桁、ハイフン等、数値に変換できない場合：-1） */
	public String ratioMarginBalance;
}
