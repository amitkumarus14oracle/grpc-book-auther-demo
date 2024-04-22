package com.restart;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {
    @Override
    public void getAuthor(Schema.Author request, StreamObserver<Schema.Author> responseObserver) {
        TempDB.getAuthorsFromTempDB()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void getBooksByAuthor(Schema.Author request, StreamObserver<Schema.Book> responseObserver) {
        TempDB.getBooksFromTempDB()
                .stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Schema.Book> getExpensiveBook(StreamObserver<Schema.Book> responseObserver) {
        return new StreamObserver<Schema.Book>() {
            Schema.Book expensiveBook = null;
            float priceTrack = 0;
            @Override
            public void onNext(Schema.Book book) {
                if(book.getPrice() > priceTrack) {
                    priceTrack = book.getPrice();
                    expensiveBook = book;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(expensiveBook);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Schema.Book> getBookByAuthorGender(StreamObserver<Schema.Book> responseObserver) {
        return new StreamObserver<Schema.Book>() {
            List<Schema.Book> bookList = new ArrayList<>();
            @Override
            public void onNext(Schema.Book book) {
                TempDB.getBooksFromTempDB()
                        .stream()
                        .filter(b -> b.getAuthorId() == book.getAuthorId())
                        .forEach(bookList::add);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                bookList.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
