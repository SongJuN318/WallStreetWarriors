package com.example.registration_login_demo.service;

import com.example.registration_login_demo.dto.SellDto;
import com.example.registration_login_demo.dto.TradingHistoryDto;
import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.Sell;
import com.example.registration_login_demo.entity.TradingHistory;
import com.example.registration_login_demo.repository.BuyUserRepository;
import com.example.registration_login_demo.repository.SellRepository;
import com.example.registration_login_demo.repository.TradingHistoryRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {
    private static final String DEFAULT_DOWNLOAD_FOLDER = "Downloads";
    private final BuyUserRepository buyUserRepository;
    private final SellRepository sellRepository;
    private final TradingHistoryRepository tradingHistoryRepository;


    @Autowired
    public ReportService(
            BuyUserRepository buyUserRepository,
            TradingHistoryRepository tradingHistoryRepository,
            SellRepository sellRepository) {
        this.buyUserRepository = buyUserRepository;
        this.tradingHistoryRepository = tradingHistoryRepository;
        this.sellRepository = sellRepository;
    }

    private static String getDownloadFolderPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, DEFAULT_DOWNLOAD_FOLDER).toString();
    }

    public void downloadAsCSV(long id) {
        try {
            BuyUser buyUser = findBuyUserById(id);
            String downloadFolder = getDownloadFolderPath();
            String filePath = Paths.get(downloadFolder, "Report.csv").toString();

            PrintWriter pw = new PrintWriter(new File(filePath));
            StringBuilder sb = new StringBuilder();

            // Append header
            sb.append("Buy History\n");
            sb.append("OrderId, Symbol, Buy Price, Lots\n");

            // Append trade history
            List<TradingHistoryDto> histories = findHistoryByUserId(buyUser.getId());
            for (TradingHistoryDto history : histories) {
                sb.append(history.getOrderId()).append(",")
                        .append(history.getSymbol()).append(",")
                        .append(history.getBuyPrice()).append(",")
                        .append(history.getLots()).append("\n");
            }
            sb.append("\nSell History\n");
            sb.append("OrderId, Symbol, Sell Price, Lots, Profit&Loss\n");

            // Append sell history
            List<SellDto> sellHistories = findSellsByUserId(buyUser.getId());
            for (SellDto history : sellHistories) {
                sb.append(history.getOrderId()).append(",")
                        .append(history.getSymbol()).append(",")
                        .append(history.getSellPrice()).append(",")
                        .append(history.getLots()).append(",")
                        .append(history.getProfit_lost()).append("\n");
            }

            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadAsTXT(long id) {
        try {
            BuyUser buyUser = findBuyUserById(id);
            String downloadFolder = getDownloadFolderPath();
            String filePath = Paths.get(downloadFolder, "Report.txt").toString();
            PrintWriter pw = new PrintWriter(new File(filePath));
            StringBuilder sb = new StringBuilder();

            sb.append("User Report :\n\n\n");
            sb.append("\nUsername                        : ").append(buyUser.getUser().getName());
            sb.append("\nBalance                         : ").append(buyUser.getCurrentFund());
            sb.append("\nCumulative Profit and Loss (P&L): ").append(buyUser.getPnl());
            sb.append("\nCumulative P&L points           : ").append(buyUser.getPoint());

            sb.append("\n\n\nTrade History: \n");
            sb.append("OrderId, Symbol, Sell Price, Lots, Profit&Loss\n\n");
            List<SellDto> sellHistories = findSellsByUserId(buyUser.getId());
            for (SellDto history : sellHistories) {
                sb.append(history.getOrderId()).append(" ");
                sb.append(history.getSymbol()).append(" ");
                sb.append(history.getSellPrice()).append(" ");
                sb.append(history.getLots()).append(" ");
                sb.append(history.getProfit_lost()).append("\n");
            }
            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadAsPdf(long id) {
        try {
            BuyUser buyUser = findBuyUserById(id);
            String downloadFolder = getDownloadFolderPath();
            String filePath = Paths.get(downloadFolder, "Report.pdf").toString();

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            var headingFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            var userInfoFont = new Font(Font.FontFamily.HELVETICA, 12);
            var tableFont = new Font(Font.FontFamily.HELVETICA, 10);

            var paragraph = new Paragraph("Trading Report", headingFont);
            paragraph.add(new Phrase("\nUsername                        : " + buyUser.getUser().getName(), userInfoFont));
            paragraph.add(new Phrase("\nBalance                         : " + buyUser.getCurrentFund(), userInfoFont));
            paragraph.add(new Phrase("\nCumulative Profit and Loss (P&L): " + buyUser.getPnl(), userInfoFont));
            paragraph.add(new Phrase("\nCumulative P&L points           : " + buyUser.getPoint(), userInfoFont));

            var table = new PdfPTable(4);
            Stream.of("OrderId", "Symbol", "Buy Price", "Lots").forEach(header -> {
                var cell = new PdfPCell(new Phrase(header, tableFont));
                table.addCell(cell);
            });

            List<TradingHistoryDto> histories = findHistoryByUserId(buyUser.getId());
            for (TradingHistoryDto history : histories) {
                table.addCell(new Phrase(String.valueOf(history.getOrderId()), tableFont));
                table.addCell(new Phrase(history.getSymbol(), tableFont));
                table.addCell(new Phrase(String.valueOf(history.getBuyPrice()), tableFont));
                table.addCell(new Phrase(String.valueOf(history.getLots()), tableFont));
            }

            paragraph.add(table);


            var table1 = new PdfPTable(5);
            Stream.of("OrderId", "Symbol", "Sell Price", "Lots", "Profit&Loss").forEach(header -> {
                var cell = new PdfPCell(new Phrase(header, tableFont));
                table1.addCell(cell);
            });

            List<SellDto> sellhistories = findSellsByUserId(buyUser.getId());
            for (SellDto history : sellhistories) {
                table1.addCell(new Phrase(String.valueOf(history.getOrderId()), tableFont));
                table1.addCell(new Phrase(history.getSymbol(), tableFont));
                table1.addCell(new Phrase(String.valueOf(history.getSellPrice()), tableFont));
                table1.addCell(new Phrase(String.valueOf(history.getLots()), tableFont));
                table1.addCell(new Phrase(String.valueOf(history.getProfit_lost()), tableFont));
            }

            paragraph.add(table1);
            doc.add(paragraph);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TradingHistoryDto> findHistoryByUserId(long userId) {
        BuyUser user = buyUserRepository.findById(userId);
        if (user != null) {
            List<TradingHistory> tradingHistories = tradingHistoryRepository.findByUser(user);
            return tradingHistories.stream()
                    .map(this::mapToTradingHistoryDto)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("User with ID " + userId + " not found.");
    }

    private TradingHistoryDto mapToTradingHistoryDto(TradingHistory tradingHistory) {
        TradingHistoryDto tradingHistoryDto = new TradingHistoryDto();
        tradingHistoryDto.setOrderId(tradingHistory.getOrderId());
        tradingHistoryDto.setUserId(tradingHistory.getUser().getId());
        tradingHistoryDto.setSymbol(tradingHistory.getSymbol());
        tradingHistoryDto.setLots(tradingHistory.getLots());
        tradingHistoryDto.setBuyPrice(tradingHistory.getBuyPrice());
        return tradingHistoryDto;
    }

    public List<SellDto> findSellsByUserId(long userId) {
        BuyUser user = buyUserRepository.findById(userId);
        if (user != null) {
            List<Sell> sells = sellRepository.findByUser(user);
            return sells.stream()
                    .map(this::mapToSellDto)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("User with ID " + userId + " not found.");
    }

    private SellDto mapToSellDto(Sell sell) {
        SellDto sellDto = new SellDto();
        sellDto.setOrderId(sell.getOrderId());
        sellDto.setUserId(sell.getUser().getId());
        sellDto.setSymbol(sell.getSymbol());
        sellDto.setLots(sell.getLots());
        sellDto.setSellPrice(sell.getBuyPrice());
        sellDto.setProfit_lost(sell.getProfitLost());
        return sellDto;
    }

    public BuyUser findBuyUserById(long id) {
        return buyUserRepository.findById(id);
    }
}


