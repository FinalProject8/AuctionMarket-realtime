//package org.example.auctionmaerketrealtime.domain.dummy;
//
//import lombok.RequiredArgsConstructor;
//import org.example.auctionmaerketrealtime.domain.auction.entity.Auction;
//import org.example.auctionmaerketrealtime.domain.auction.repository.AuctionRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//public class DummyDataLoader implements CommandLineRunner {
//
//    private final AuctionRepository auctionRepository;
//
//    @Override
//    public void run(String... args) {
//        if (auctionRepository.count() == 0) {
//            auctionRepository.save(Auction.builder()
//                    .title("테스트 경매")
//                    .topPrice(1000L)
//                    .startTime(LocalDateTime.now())
//                    .endTime(LocalDateTime.now().plusMinutes(20))
//                    .build());
//            System.out.println("테스트 경매 등록 완료 (ID = 1)");
//        }
//    }
//}
