import java.util.*;
import java.io.*;
import java.nio.file.Paths;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    private static int getCommodityIndex(String name) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            String filePath = "Data_Files/" + months[m] + ".txt";
            Scanner sc = null;


            try {
                sc = new Scanner(Paths.get(filePath));

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        try {
                            int day = Integer.parseInt(parts[0].trim());
                            String commName = parts[1].trim();
                            int profit = Integer.parseInt(parts[2].trim());

                            int dayIndex = day - 1; // 1-28 -> 0-27
                            int commIndex = getCommodityIndex(commName);

                            if (dayIndex >= 0 && dayIndex < DAYS && commIndex != -1) {
                                profitData[m][dayIndex][commIndex] = profit;
                            }
                        } catch (NumberFormatException e) {
                            e.toString();
                        }
                    }
                }
            } catch (IOException e) {

            } finally {

                if (sc != null) {
                    sc.close();
                }
            }
        }
    }


    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        int maxProfit = Integer.MIN_VALUE;
        int bestCommodityIndex = -1;
        for (int c = 0; c < COMMS; c++) {
            int currentTotal = 0;
            for (int d = 0; d < DAYS; d++) {
                currentTotal += profitData[month][d][c];
            }
            if (currentTotal > maxProfit) {
                maxProfit = currentTotal;
                bestCommodityIndex = c;
            }
        }
        if (bestCommodityIndex == -1) return "No Data 0";
        return commodities[bestCommodityIndex] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -99999;
        int dayIndex = day - 1;
        int total = 0;
        for (int c = 0; c < COMMS; c++) {
            total += profitData[month][dayIndex][c];
        }
        return total;
    }

    public static int commodityProfitInRange(String commodity, int fromDay, int toDay) {
        int cIndex = getCommodityIndex(commodity);
        if (cIndex == -1 || fromDay < 1 || toDay > DAYS || fromDay > toDay) return -99999;
        int total = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = fromDay - 1; d < toDay; d++) {
                total += profitData[m][d][cIndex];
            }
        }
        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1;
        int maxProfit = Integer.MIN_VALUE;
        int bestDay = -1;
        for (int d = 0; d < DAYS; d++) {
            int dayTotal = 0;
            for (int c = 0; c < COMMS; c++) {
                dayTotal += profitData[month][d][c];
            }
            if (dayTotal > maxProfit) {
                bestDay = d;
                maxProfit = dayTotal;
            }
        }
        return bestDay + 1;
    }

    public static String bestMonthForCommodity(String commodity) {
        int cIndex = getCommodityIndex(commodity);
        if (cIndex == -1) return "INVALID_COMMODITY";
        int maxProfit = Integer.MIN_VALUE;
        int bestMonth = -1;
        for (int m = 0; m < MONTHS; m++) {
            int monthTotal = 0;
            for (int d = 0; d < DAYS; d++) {
                monthTotal += profitData[m][d][cIndex];
            }
            if (monthTotal > maxProfit) {
                maxProfit = monthTotal;
                bestMonth = m;
            }
        }
        return months[bestMonth];
    }

    public static int consecutiveLossDays(String commodity) {
        int cIndex = getCommodityIndex(commodity);
        if (cIndex == -1) return -1;
        int maxStreak = 0;
        int currentStreak = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profitData[m][d][cIndex] < 0) {
                    currentStreak++;
                } else {
                    if (currentStreak > maxStreak) {
                        maxStreak = currentStreak;
                    }
                    currentStreak = 0;
                }
            }
        }
        if (currentStreak > maxStreak) maxStreak = currentStreak;
        return maxStreak;
    }

    public static int daysAboveThreshold(String commodity, int threshold) {
        int cIndex = getCommodityIndex(commodity);
        if (cIndex == -1) return -1;
        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profitData[m][d][cIndex] > threshold) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int biggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS) return -99999;
        int maxSwing = 0;
        for (int d = 0; d < DAYS - 1; d++) {
            int today = 0;
            int tommorow = 0;
            for (int c = 0; c < COMMS; c++) {
                today = profitData[month][d][c];
                tommorow = profitData[month][d + 1][c];
            }
            int swing = Math.abs(today - tommorow);
            if (maxSwing < swing) {
                maxSwing = swing;
            }
        }
        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int index1 = getCommodityIndex(c1);
        int index2 = getCommodityIndex(c2);
        if (index1 == -1 || index2 == -1) return "INVALID_COMMODITY";
        int total1 = 0;
        int total2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                total1 += profitData[m][d][index1];
                total2 += profitData[m][d][index2];
            }
        }
        if (total1 > total2) {
            return c1 + " is better by " + (total1 - total2);
        }
        if (total2 > total1) {
            return c2 + " is better by " + (total2 - total1);
        }
        return "Equal";
    }

    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        int[] weeks = new int[4];
        for (int d = 0; d < DAYS; d++) {
            int dailyTotal = 0;
            for (int c = 0; c < COMMS; c++) {
                dailyTotal += profitData[month][d][c];
            }
            weeks[d / 7] += dailyTotal;
        }
        int greatestWeek = Integer.MIN_VALUE;
        int bestWeekIndex = -1;
        for (int w = 0; w < 4; w++) {
            if (weeks[w] > greatestWeek) {
                greatestWeek = weeks[w];
                bestWeekIndex = w;
            }
        }
        return "Week " + (bestWeekIndex + 1);
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}
