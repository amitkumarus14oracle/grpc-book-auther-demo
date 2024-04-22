package com.restart.service;

import com.google.protobuf.Descriptors;
import com.restart.BookAuthorServiceGrpc;
import com.restart.Schema;
import com.restart.Schema.Author;
import com.restart.TempDB;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class BookAuthorClientService {
    @GrpcClient("grpc-service")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-service")
    BookAuthorServiceGrpc.BookAuthorServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor,Object> getAuthor(int authorId) {
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        Author authorResponse = synchronousClient.getAuthor(authorRequest);
        return authorResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getBookByAuthor(int authorId) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        asynchronousClient.getBooksByAuthor(authorRequest, new StreamObserver<Schema.Book>() {
            @Override
            public void onNext(Schema.Book book) {
                response.add(book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.EMPTY_LIST;
    }

    public Map<String, Map<Descriptors.FieldDescriptor,Object>> getExpensiveBook() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Map<String, Map<Descriptors.FieldDescriptor,Object>> response = new HashMap<>();
        StreamObserver<Schema.Book> responseObserver = asynchronousClient.getExpensiveBook(new StreamObserver<Schema.Book>() {
            @Override
            public void onNext(Schema.Book book) {
                response.put("Expensive Book", book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        TempDB.getBooksFromTempDB().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.EMPTY_MAP;
    }

    public List<Map<Descriptors.FieldDescriptor,Object>> getBooksByAuthorGender(String gender) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor,Object>> response = new ArrayList<>();
        StreamObserver<Schema.Book> responseObserver =  asynchronousClient.getBookByAuthorGender(new StreamObserver<Schema.Book>() {
            @Override
            public void onNext(Schema.Book book) {
                response.add(book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        TempDB.getAuthorsFromTempDB()
                .stream()
                .filter(author -> author.getGender().equals(gender))
                .forEach(author -> responseObserver.onNext(Schema.Book.newBuilder().setAuthorId(author.getAuthorId()).build()));

        responseObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.EMPTY_LIST;
    }
}
