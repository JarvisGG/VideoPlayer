//package com.jarvis.videoplayer.http;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.Pair;
//
//import com.annimon.stream.Optional;
//import com.annimon.stream.Stream;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.jarvis.model.DailyNews;
//import com.jarvis.model.Question;
//import com.jarvis.model.Story;
//import com.jarvis.videoplayer.rx.RxSchedulers;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.reactivestreams.Publisher;
//import org.reactivestreams.Subscriber;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.BackpressureStrategy;
//import io.reactivex.Flowable;
//import io.reactivex.FlowableEmitter;
//import io.reactivex.FlowableOnSubscribe;
//import io.reactivex.Observable;
//import io.reactivex.Single;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//import static com.jarvis.videoplayer.http.Constants.Urls.BASE_URL;
//import static com.jarvis.videoplayer.http.Constants.Urls.ZHIHU_DAILY_BEFORE;
//import static com.jarvis.videoplayer.http.Constants.Urls.ZHIHU_DAILY_OFFLINE_NEWS;
//
///**
// * @author Jarvis
// * @version 1.0
// * @title VideoPlayer
// * @description 该类主要功能描述
// * @company 北京奔流网络技术有限公司
// * @create 2017/7/29 下午10:35
// * @changeRecord [修改记录] <br/>
// */
//
//public class ApiImpl {
//    private static final String TAG = "ApiImpl";
//    public static final String CHARSET = "UTF-8";
//
//    private static final String QUESTION_SELECTOR = "div.question";
//    private static final String QUESTION_TITLES_SELECTOR = "h2.question-title";
//    private static final String QUESTION_LINKS_SELECTOR = "div.view-more a";
//
//    private Api restRxApi;
//    private static ApiImpl mInstance;
//
//    public ApiImpl(Context context) {
//        initRestApi();
//    }
//
//    public static ApiImpl getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new ApiImpl(context);
//        }
//        return mInstance;
//    }
//
//    private void initRestApi() {
//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .retryOnConnectionFailure(true)
//                .addNetworkInterceptor(new HttpLoggingInterceptor(s -> Log.e(TAG, "----->" + s))
//                .setLevel(HttpLoggingInterceptor.Level.BODY))
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit restAdapter =
//                new Retrofit.Builder()
//                        .client(httpClient)
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                        .build();
//        restRxApi = restAdapter.create(Api.class);
//    }
//
//    public static Flowable<List<DailyNews>> ofDate(String date) {
//        Flowable<Story> stories = getHtml(Constants.Urls.ZHIHU_DAILY_BEFORE, date)
//                .flatMap(ApiImpl::getStoriesJsonArrayObservable)
//                .flatMap(ApiImpl::getStoriesObservable);
//
//        Flowable<Document> documents = stories
//                .flatMap(ApiImpl::getDocumentObservable);
//
//        Flowable<Optional<Pair<Story, Document>>> optionalStoryNDocuments
//                = Flowable.zip(stories, documents, ApiImpl::createPair);
//
//        Flowable<Pair<Story, Document>> storyNDocuments = toNonempty(optionalStoryNDocuments);
//
//        return toNonempty(storyNDocuments.map(ApiImpl::convertToDailyNews))
//                .doOnNext(news -> news.setDate(date))
//                .toList().toFlowable();
//    }
//
//    private static Flowable<JSONArray> getStoriesJsonArrayObservable(String html) {
//        return Flowable.create(subscriber -> {
//            try {
//                subscriber.onNext(new JSONObject(html).getJSONArray("stories"));
//            } catch (JSONException e) {
//                subscriber.onError(e);
//            }
//        }, BackpressureStrategy.DROP);
//    }
//
//    private static Flowable<Story> getStoriesObservable(JSONArray newsArray) {
//        return Flowable.create(subscriber -> {
//            try {
//                for (int i = 0; i < newsArray.length(); i++) {
//                    JSONObject newsJson = newsArray.getJSONObject(i);
//                    subscriber.onNext(getStoryFromJSON(newsJson));
//                }
//
//            } catch (JSONException e) {
//                subscriber.onError(e);
//            }
//        }, BackpressureStrategy.DROP);
//    }
//
//    private static Story getStoryFromJSON(JSONObject jsonStory) throws JSONException {
//        Story story = new Story();
//
//        story.setStoryId(jsonStory.getInt("id"));
//        story.setDailyTitle(jsonStory.getString("title"));
//        story.setThumbnailUrl(getThumbnailUrlForStory(jsonStory));
//
//        return story;
//    }
//
//    private static String getThumbnailUrlForStory(JSONObject jsonStory) throws JSONException {
//        if (jsonStory.has("images")) {
//            return (String) jsonStory.getJSONArray("images").get(0);
//        } else {
//            return null;
//        }
//    }
//
//    private static Flowable<Document> getDocumentObservable(Story news) {
//        return getHtml(Constants.Urls.ZHIHU_DAILY_OFFLINE_NEWS, news.getStoryId()+"")
//                .map(ApiImpl::getStoryDocument);
//    }
//
//    private static Document getStoryDocument(String json) {
//        try {
//            JSONObject newsJson = new JSONObject(json);
//            return newsJson.has("body") ? Jsoup.parse(newsJson.getString("body")) : null;
//        } catch (JSONException e) {
//            return null;
//        }
//    }
//
//    private static Optional<Pair<Story, Document>> createPair(Story story, Document document) {
//        return Optional.ofNullable(document == null ? null : Pair.create(story, document));
//    }
//
//    private static Optional<DailyNews> convertToDailyNews(Pair<Story, Document> pair) {
//        DailyNews result = null;
//
//        Story story = pair.first;
//        Document document = pair.second;
//        String dailyTitle = story.getDailyTitle();
//
//        List<Question> questions = getQuestions(document, dailyTitle);
//        if (Stream.of(questions).allMatch(Question::isValidZhihuQuestion)) {
//            result = new DailyNews();
//
//            result.setDailyTitle(dailyTitle);
//            result.setThumbnailUrl(story.getThumbnailUrl());
//            result.setQuestions(questions);
//        }
//
//        return Optional.ofNullable(result);
//    }
//
//    private static List<Question> getQuestions(Document document, String dailyTitle) {
//        List<Question> result = new ArrayList<>();
//        Elements questionElements = getQuestionElements(document);
//
//        for (Element questionElement : questionElements) {
//            Question question = new Question();
//
//            String questionTitle = getQuestionTitleFromQuestionElement(questionElement);
//            String questionUrl = getQuestionUrlFromQuestionElement(questionElement);
//            // Make sure that the question's title is not empty.
//            questionTitle = TextUtils.isEmpty(questionTitle) ? dailyTitle : questionTitle;
//
//            question.setTitle(questionTitle);
//            question.setUrl(questionUrl);
//
//            result.add(question);
//        }
//
//        return result;
//    }
//
//        static <T> Flowable<T> toNonempty(Flowable<Optional<T>> optionalObservable) {
//        return optionalObservable.filter(Optional::isPresent).map(Optional::get);
//    }
//    private static Elements getQuestionElements(Document document) {
//        return document.select(QUESTION_SELECTOR);
//    }
//
//    private static String getQuestionTitleFromQuestionElement(Element questionElement) {
//        Element questionTitleElement = questionElement.select(QUESTION_TITLES_SELECTOR).first();
//
//        if (questionTitleElement == null) {
//            return null;
//        } else {
//            return questionTitleElement.text();
//        }
//    }
//
//    private static String getQuestionUrlFromQuestionElement(Element questionElement) {
//        Element viewMoreElement = questionElement.select(QUESTION_LINKS_SELECTOR).first();
//
//        if (viewMoreElement == null) {
//            return null;
//        } else {
//            return viewMoreElement.attr("href");
//        }
//    }
//
////    public Flowable<List<DailyNews>> getNews(String date) {
////        Flowable<Story> stories = Flowable.create((FlowableOnSubscribe<String>) subscriber -> subscriber.onNext(get(ZHIHU_DAILY_BEFORE+date)), BackpressureStrategy.DROP)
////                .flatMap(new Function<String, Publisher<JSONArray>>() {
////                    @Override
////                    public Publisher<JSONArray> apply(@io.reactivex.annotations.NonNull String s1) throws Exception {
////                        return s -> {
////                            Log.e(TAG, s1);
////                            try {
////                                s.onNext(new JSONObject(s1).getJSONArray("stories"));
////                            } catch (JSONException e) {
////                                s.onError(e);
////                            }
////                        };
////                    }
////                })
////                .flatMap(new Function<JSONArray, Publisher<Story>>() {
////                    @Override
////                    public Publisher<Story> apply(@io.reactivex.annotations.NonNull JSONArray js) throws Exception {
////                        return s -> {
////                            try {
////                                for (int i = 0; i < js.length(); i++) {
////                                    JSONObject newsJson = js.getJSONObject(i);
////                                    s.onNext(getStoryFromJSON(newsJson));
////                                }
////                            } catch (JSONException e) {
////                                s.onError(e);
////                            }
////                        };
////                    }
////                });
////        Flowable<Document> documents = stories
////                .flatMap(new Function<Story, Publisher<Document>>() {
////                    @Override
////                    public Publisher<Document> apply(@io.reactivex.annotations.NonNull Story story) throws Exception {
////
////                        return Flowable.create(new FlowableOnSubscribe<String>() {
////                            @Override
////                            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<String> e) throws Exception {
////                                e.onNext(get(ZHIHU_DAILY_OFFLINE_NEWS+story.getStoryId()));
////                            }
////                        }, BackpressureStrategy.DROP)
////                                .map(s12 -> {
////                                    Log.e(TAG, s12);
////                            try {
////                                JSONObject newsJson = new JSONObject(s12);
////                                return newsJson.has("body") ? Jsoup.parse(newsJson.getString("body")) : null;
////                            } catch (JSONException e) {
////                                return null;
////                            }
////                        });
////
//////                        return s ->
//////                            restRxApi.getOfflineNews(story.getStoryId()).map(s12 -> {
//////                                try {
//////                                    JSONObject newsJson = new JSONObject(s12);
//////                                    return newsJson.has("body") ? Jsoup.parse(newsJson.getString("body")) : null;
//////                                } catch (JSONException e) {
//////                                    return null;
//////                                }
//////                            });
////                    }
////                });
////        Flowable<Optional<Pair<Story, Document>>> optionalStoryNDocuments
////                = Flowable.zip(stories, documents, ApiImpl::createPair);
////
////        Flowable<Pair<Story, Document>> storyNDocuments = toNonempty(optionalStoryNDocuments);
////
////        return toNonempty(storyNDocuments.map(ApiImpl::convertToDailyNews))
////                .doOnNext(dailyNews -> {
////                    dailyNews.setDate(date);
////                })
////                .toList().toFlowable();
////    }
////
////    private static Optional<Pair<Story, Document>> createPair(Story story, Document document) {
////        return Optional.ofNullable(document == null ? null : Pair.create(story, document));
////    }
////
////    static <T> Flowable<T> toNonempty(Flowable<Optional<T>> optionalObservable) {
////        return optionalObservable.filter(Optional::isPresent).map(Optional::get);
////    }
////
////    private static Optional<DailyNews> convertToDailyNews(Pair<Story, Document> pair) {
////        DailyNews result = null;
////
////        Story story = pair.first;
////        Document document = pair.second;
////        String dailyTitle = story.getDailyTitle();
////
////        List<Question> questions = getQuestions(document, dailyTitle);
////        if (Stream.of(questions).allMatch(Question::isValidZhihuQuestion)) {
////            result = new DailyNews();
////
////            result.setDailyTitle(dailyTitle);
////            result.setThumbnailUrl(story.getThumbnailUrl());
////            result.setQuestions(questions);
////        }
////
////        return Optional.ofNullable(result);
////    }
////
////    private static List<Question> getQuestions(Document document, String dailyTitle) {
////        List<Question> result = new ArrayList<>();
////        Elements questionElements = getQuestionElements(document);
////
////        for (Element questionElement : questionElements) {
////            Question question = new Question();
////
////            String questionTitle = getQuestionTitleFromQuestionElement(questionElement);
////            String questionUrl = getQuestionUrlFromQuestionElement(questionElement);
////            // Make sure that the question's title is not empty.
////            questionTitle = TextUtils.isEmpty(questionTitle) ? dailyTitle : questionTitle;
////
////            question.setTitle(questionTitle);
////            question.setUrl(questionUrl);
////
////            result.add(question);
////        }
////
////        return result;
////    }
////
////    private static Elements getQuestionElements(Document document) {
////        return document.select(QUESTION_SELECTOR);
////    }
////
////    private static String getQuestionTitleFromQuestionElement(Element questionElement) {
////        Element questionTitleElement = questionElement.select(QUESTION_TITLES_SELECTOR).first();
////
////        if (questionTitleElement == null) {
////            return null;
////        } else {
////            return questionTitleElement.text();
////        }
////    }
////
////    private static String getQuestionUrlFromQuestionElement(Element questionElement) {
////        Element viewMoreElement = questionElement.select(QUESTION_LINKS_SELECTOR).first();
////
////        if (viewMoreElement == null) {
////            return null;
////        } else {
////            return viewMoreElement.attr("href");
////        }
////    }
////
////    private static Story getStoryFromJSON(JSONObject jsonStory) throws JSONException {
////        Story story = new Story();
////
////        story.setStoryId(jsonStory.getInt("id"));
////        story.setDailyTitle(jsonStory.getString("title"));
////        story.setThumbnailUrl(getThumbnailUrlForStory(jsonStory));
////
////        return story;
////    }
////
////    private static String getThumbnailUrlForStory(JSONObject jsonStory) throws JSONException {
////        if (jsonStory.has("images")) {
////            return (String) jsonStory.getJSONArray("images").get(0);
////        } else {
////            return null;
////        }
////    }
//
//    private static String encodeString(String str) {
//        try {
//            return URLEncoder.encode(str, CHARSET);
//        } catch (UnsupportedEncodingException ignored) {
//            return "";
//        }
//    }
//
//    public static String get(String address) {
//        URL url = null;
//        try {
//            url = new URL(address);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", "Mozilla/5.0");
//
//            try {
//                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//
//                    in.close();
//
//                    return response.toString();
//                } else {
//                    throw new IOException("Network Error - response code: " + con.getResponseCode());
//                }
//            } finally {
//                con.disconnect();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    static Flowable<String> getHtml(String url, String suffix) {
//        return Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<String> sub) throws Exception {
//                sub.onNext(get(url + suffix));
//            }
//
//        }, BackpressureStrategy.DROP);
//    }
//}
