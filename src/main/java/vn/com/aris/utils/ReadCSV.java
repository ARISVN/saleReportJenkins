package vn.com.aris.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;

import vn.com.aris.constant.Constant;
import vn.com.aris.constant.DateType;
import vn.com.aris.dto.AppleChartDTO;
import vn.com.aris.mapper.file.AndroidReportOverview;
import vn.com.aris.mapper.file.AndroidReportSale;
import vn.com.aris.mapper.file.ReportAppleData;
import vn.com.aris.model.AndroidAppKey;
import vn.com.aris.model.AndroidItemKey;
import vn.com.aris.model.AppSaleDaily;
import vn.com.aris.model.DataOnRow;
import vn.com.aris.model.DataReportAndroid;
import vn.com.aris.model.ItemUnique;
import vn.com.aris.model.ObjectParsed;
import vn.com.aris.model.ObjectResultSale;
import vn.com.aris.model.RowSaleReport;
import vn.com.aris.model.RowUnique;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class ReadCSV {
	static final String FORMAT_DAY_ANDROID = "yyyy-MM-dd";

	public static String getDataFromCSV(String path) {
		StringBuilder buil = new StringBuilder();
		buil.append("[");
		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		File csvFile = new File(path);
		try {
			MappingIterator<Object[]> it = mapper.reader(Object[].class).readValues(csvFile);
			if (it.hasNext()) {
				it.next();
			}
			while (it.hasNext()) {
				Object[] nextLine = it.next();
				String date = (String) nextLine[AndroidReportOverview.DATE];
				String curDevIns = (String) nextLine[AndroidReportOverview.CURRENT_DEVICE_INSTALLS];
				buil.append("[");
				buil.append("'");
				buil.append(date.toString());
				buil.append("'");
				buil.append(",");
				buil.append(curDevIns);
				buil.append("]");
				buil.append(",");

			}
			buil.append("]");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buil.toString();
	}

	public static List<AppSaleDaily> getDataFromCSVOverview(String path, Long appId) throws Exception {
		List<AppSaleDaily> result = new ArrayList<AppSaleDaily>();
		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		File csvFile = new File(path);
		try {
			MappingIterator<Object[]> it = mapper.reader(Object[].class).readValues(csvFile);
			if (it.hasNext()) {
				it.next();
			}
			while (it.hasNext()) {
				AppSaleDaily appSale = new AppSaleDaily();
				Object[] nextLine = it.next();
				String date = (String) nextLine[AndroidReportOverview.DATE];
				String dalDevIns = (String) nextLine[AndroidReportOverview.DAILY_DEVICE_INSTALLS];
				appSale.setDate(ConvertData.convertFromStringToDate(date, FORMAT_DAY_ANDROID));
				appSale.setNumberInstall(Integer.parseInt(dalDevIns));
				appSale.setAppId(appId);
				appSale.setSale(0.0);
				result.add(appSale);
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static AppleChartDTO getDataFromTxt(String pathGz, String pathUnGzip) {
		AppleChartDTO dto = new AppleChartDTO();
		StringBuilder unitArr = new StringBuilder();
		unitArr.append("[");
		StringBuilder nameArr = new StringBuilder();
		nameArr.append("[");
		try {
			doUnGzip(pathGz, pathUnGzip);
			Map<String, Object[]> map = new HashMap<String, Object[]>();
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.schemaFor(ReportAppleData.class).withColumnSeparator('\t').withLineSeparator("\n");
			MappingIterator<ReportAppleData> it = mapper.reader(ReportAppleData.class).with(schema).readValues(new File(pathUnGzip));

			if (it.hasNextValue()) {
				it.nextValue();
			}
			int count = 0;
			while (it.hasNextValue()) {
				ReportAppleData next = it.nextValue();
				if (count == 0) {
					dto.setBeginDate(next.getC19BeginDate());
					dto.setEndDate(next.getC21EndDate());
				}
				count++;
				String c17Units = next.getC17Units();
				int unit = Integer.parseInt(c17Units);
				if (map.containsKey(next.getC12sku())) {
					Object[] valueObj = map.get(next.getC12sku());
					Integer value = (Integer) valueObj[1];
					unit += value;

				}
				Object[] obj = { next.getC14Title(), unit };
				map.put(next.getC12sku(), obj);

			}

			for (Map.Entry<String, Object[]> entry : map.entrySet()) {
				entry.getKey();
				entry.getValue();
				nameArr.append("\"" + entry.getValue()[0] + "\"");

				nameArr.append(",");

				unitArr.append(entry.getValue()[1]);
				unitArr.append(",");
			}
			unitArr.append("]");
			nameArr.append("]");
			dto.setUnitArray(unitArr.toString());
			dto.setNameApp(nameArr.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public static ObjectParsed getDataFromTxt2(String pathGz, String pathUnGzip, String appCode) {
		ObjectParsed result = new ObjectParsed();
		String beginDate = "";
		String endDate = "";
		Map<RowUnique, DataOnRow> mapApp = new HashMap<RowUnique, DataOnRow>();
		Map<ItemUnique, DataOnRow> mapItem = new HashMap<ItemUnique, DataOnRow>();
		try {
			doUnGzip(pathGz, pathUnGzip);
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.schemaFor(ReportAppleData.class).withColumnSeparator('\t').withLineSeparator("\n");
			MappingIterator<ReportAppleData> it = mapper.reader(ReportAppleData.class).with(schema).readValues(new File(pathUnGzip));

			if (it.hasNextValue()) {
				it.nextValue();
			}
			int count = 0;
			while (it.hasNextValue()) {
				DataOnRow data = new DataOnRow();
				ReportAppleData next = it.nextValue();
				if (count == 0) {
					beginDate = next.getC19BeginDate();
					endDate = next.getC21EndDate();
				}
				count++;
				String sku = next.getC12sku().trim();
				String parentIdentifier = next.getC28ParentIdentifier().trim();
				String c17Units = next.getC17Units();
				int unit = Integer.parseInt(c17Units);
				Double developerProceeds = Double.parseDouble(next.getC18DeveloperProceeds());// Sale
																								// nhung
																								// chua
																								// biet
																								// la
																								// cua
																								// item
																								// hay
																								// app
				String c24CurrencyOfProceeds = next.getC24CurrencyOfProceeds().trim();
				if (StringUtils.isNotEmpty(parentIdentifier) && !parentIdentifier.equals("")) {// record
																								// is
																								// item
					if (parentIdentifier.equals(appCode)) {
						if (mapItem.containsKey(new ItemUnique(sku, c24CurrencyOfProceeds, parentIdentifier))) {
							DataOnRow valueItem = mapItem.get(new ItemUnique(sku, c24CurrencyOfProceeds, parentIdentifier));
							Double sale = valueItem.getSale();
							developerProceeds += sale;
							Integer boughtNumber = (Integer) valueItem.getUnit();
							unit += boughtNumber;
						}
						data.setBeginDate(beginDate);
						data.setEndDate(endDate);
						data.setUnit(unit);
						data.setSale(developerProceeds);
						data.setCurrency(c24CurrencyOfProceeds);
						data.setItemCode(sku);
						data.setItemName(next.getC14Title());
						mapItem.put(new ItemUnique(sku, c24CurrencyOfProceeds, parentIdentifier), data);
						// System.out.println("Item:   " + sku);
						System.out.println("So lan trung" + sku + "/" + c24CurrencyOfProceeds + "/" + parentIdentifier + "/" + unit);
					}

				} else {// record is app
					if (sku.equals(appCode)) {
						if (mapApp.containsKey(new RowUnique(sku, c24CurrencyOfProceeds))) {
							DataOnRow valueObj = mapApp.get(new RowUnique(sku, c24CurrencyOfProceeds));
							Double revenue = valueObj.getSale();
							developerProceeds += revenue;

							Integer value = (Integer) valueObj.getUnit();
							unit += value;
						}
						data.setBeginDate(beginDate);
						data.setEndDate(endDate);
						data.setUnit(unit);
						data.setSale(developerProceeds);
						data.setCurrency(c24CurrencyOfProceeds);
						mapApp.put(new RowUnique(sku, c24CurrencyOfProceeds), data);
						// System.out.println("App:   " + sku);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setDataApp(mapApp);
		result.setDataItem(mapItem);
		if (beginDate.equals(endDate)) {
			result.setDateType(DateType.DAILY);
		} else {
			result.setDateType(DateType.YEARLY);
		}
		return result;
	}

	private static void doUnGzip(String pathGz, String pathUnGzip) throws FileNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(pathGz);
		GZIPInputStream gzin = new GZIPInputStream(fin);
		FileOutputStream fout = new FileOutputStream(pathUnGzip);
		for (int c = gzin.read(); c != -1; c = gzin.read()) {
			fout.write(c);
		}
		fout.close();
		gzin.close();
	}

	public static DataReportAndroid getDataAndroidFromFile(String pathOverview, String pathSale, Long appId, String appCode) throws Exception {

		List<AppSaleDaily> dataFromCSVOverview = getDataFromCSVOverview(pathOverview, appId);
		ObjectResultSale result = getDataFromCSVSale(pathSale, appCode);
		Map<AndroidItemKey, RowSaleReport> mapSaleItem = result.getMapSaleItem();
		// List<AppSaleDaily> lstAppSale = new ArrayList<AppSaleDaily>();

		Map<AndroidAppKey, RowSaleReport> mapSaleApp = result.getMapSaleApp();
		for (Map.Entry<AndroidAppKey, RowSaleReport> entry : mapSaleApp.entrySet()) {
			AndroidAppKey key = entry.getKey();
			RowSaleReport value = entry.getValue();
			for (int i = 0; i < dataFromCSVOverview.size(); i++) {
				if (ConvertData.convertFromStringToDate(key.getOrderChargedDate(), FORMAT_DAY_ANDROID).equals(dataFromCSVOverview.get(0).getDate())) {
					dataFromCSVOverview.get(0).setCurrency(key.getCurrencyOfSale());
					dataFromCSVOverview.get(0).setSale(value.getSale());
					break;
				}
			}
		}
		return new DataReportAndroid(dataFromCSVOverview, mapSaleItem);

	}

	private static ObjectResultSale getDataFromCSVSale(String pathSale, String appCode) {
		ObjectResultSale result = new ObjectResultSale();
		Map<AndroidAppKey, RowSaleReport> mapSaleApp = new HashMap<AndroidAppKey, RowSaleReport>();
		Map<AndroidItemKey, RowSaleReport> mapSaleItem = new HashMap<AndroidItemKey, RowSaleReport>();
		if (pathSale != null) {
			CsvMapper mapper = new CsvMapper();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			ReadCSV read = new ReadCSV();
			String unZipIt = read.unZipIt(pathSale);
			File csvFile = new File(unZipIt);
			try {

				// mapper.readValues(new InputStreamReader(new
				// FileInputStream(csvFile), "ISO-8859-1");
				MappingIterator<Object[]> it = mapper.reader(Object[].class).readValues(csvFile);
				if (it.hasNext()) {
					it.next();
				}
				while (it.hasNext()) {
					RowSaleReport row = new RowSaleReport();
					Object[] nextLine = it.next();
					String currency = (String) nextLine[AndroidReportSale.CURRENCY_OF_SALE];
					String itemPriceStr = (String) nextLine[AndroidReportSale.ITEM_PRICE];
					Double price = Double.parseDouble(itemPriceStr.replace(",", ""));
					String date = (String) nextLine[AndroidReportSale.ORDER_CHARGED_DATE];
					String productId = (String) nextLine[AndroidReportSale.PRODUCT_ID];
					String productTitle = (String) nextLine[AndroidReportSale.PRODUCT_TITLE];
					String sku = (String) nextLine[AndroidReportSale.SKU_ID];
					AndroidAppKey keyApp = new AndroidAppKey(productId, currency, date);
					AndroidItemKey keyItem = new AndroidItemKey(productId, sku, currency, date);
					if (sku != null)// record is item
					{
						if (productId.equals(appCode)) {
							if (mapSaleItem.containsKey(keyItem)) {
								RowSaleReport rowSaleReport = mapSaleItem.get(keyItem);
								Double sale = rowSaleReport.getSale();
								int boughtNumber = rowSaleReport.getBoughtNumber();
								boughtNumber += 1;
								price += sale;
								row.setBoughtNumber(boughtNumber);
								row = rowSaleReport;
							} else {
								row.setBoughtNumber(1);
								row.setCurrency(currency);
								row.setItemCode(sku);
								row.setItemName(productTitle);

							}
							row.setSale(price);
							mapSaleItem.put(keyItem, row);
						}

					} else// record is app
					{
						if (mapSaleApp.containsKey(keyApp)) {
							RowSaleReport rowSaleReport = mapSaleApp.get(keyApp);
							Double sale = rowSaleReport.getSale();
							price += sale;
							row = rowSaleReport;
						} else {
							row.setCurrency(currency);
						}
						row.setSale(price);
						mapSaleApp.put(keyApp, row);
					}
				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		result.setMapSaleApp(mapSaleApp);
		result.setMapSaleItem(mapSaleItem);
		return result;
	}

	public static ObjectParsed getDataFromFile(String path, String appCode) {
		int indexOf = path.lastIndexOf(".");
		String pathUnGzip = path.substring(0, indexOf);
		ObjectParsed dataFromTxt = getDataFromTxt2(path, pathUnGzip, appCode);
		return dataFromTxt;
	}

	public static void main(String[] args) {
		// getDataFromFile(ApplicationType.IOS,
		// "D:\\S_D_85569551_20151104.txt.gz");
		// getDataFromFile("D:\\installs_com.arisvn.arissmarthiddenbox_201508_overview.csv");
		// getDataFromFile(ApplicationType.IOS,
		// "D:\\S_M_85569551_201509.txt.gz");
		// getDataFromFile(ApplicationType.IOS, "D:\\S_Y_85569551_2014.txt.gz");
		// String file = "salesreport_201511.zip";
		// ReadCSV read = new ReadCSV();
		// read.unZipIt("D:\\gsutil\\gsutil\\data\\salesreport_201511.zip",
		// OUTPUT_FOLDER);
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param outputFolder
	 *            zip file output folder
	 */
	public String unZipIt(String zipFile) {
		int lastIndexOf = zipFile.lastIndexOf(File.separator);
		String forder = zipFile.substring(0, lastIndexOf);
		String fileExt = zipFile.substring(lastIndexOf + 1, zipFile.length());
		byte[] buffer = new byte[1024];
		try {
			// create output directory is not exists
			File folder = new File(forder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(forder + File.separator + fileName);
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return forder + File.separator + fileExt.replace(Constant.GOOGLE_SALE_REPORT_EXTENSION, Constant.CSV_EXTENSION);
	}
}
