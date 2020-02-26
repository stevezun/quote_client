package com.ebookfrenzy.quoteclient.service;

import com.ebookfrenzy.quoteclient.model.Quote;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuoteRepository {


  private static final int NETWORK_POOL_SIZE = 10;

  private final QodService proxy;
  private final Executor newtworkPool;

  private QuoteRepository() {
    proxy = QodService.getInstance();
    newtworkPool = Executors.newFixedThreadPool(NETWORK_POOL_SIZE);
  }

  public static QuoteRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<Quote> getRandom() {
    return proxy.getRandom()
        .subscribeOn( Schedulers.from( newtworkPool ) );
  }

  private static class InstanceHolder {

    private static final QuoteRepository INSTANCE = new QuoteRepository();
  }
}