package com.trendingnews.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trendingnews.R;
import com.trendingnews.adapter.MultiMediaAdapter;
import com.trendingnews.adapter.PremiumAdapter;
import com.trendingnews.config.Network;
import com.trendingnews.config.OnTaskDoneListener;
import com.trendingnews.config.WebServicePost;
import com.trendingnews.model.ArticleModel;
import com.trendingnews.model.MediaInnerModel;
import com.trendingnews.model.MultiMediaModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class FragmentMultiMedia extends Fragment {

    View rootView;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    private ArrayList<MultiMediaModel> articleModels;
    private MultiMediaAdapter imageAdapter = null;


    TextView tv_no_data_found;
    String currentDateandTime = "";
    Date date1;
    Date date2;
    SimpleDateFormat sdf;

    int page = 1;
    boolean EndPage = false;
    private NestedScrollView nestedSV;

    String Id = "";
    String Type = "";

    @Override
    public void onStart() {
        super.onStart();
        EndPage = false;
        page = 1;
        articleModels = new ArrayList<>();
        getData(Id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_multi_media, container, false);

        tv_no_data_found = rootView.findViewById(R.id.tv_no_data_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            System.out.println("key: " + bundle.getString("Id"));
            if (bundle.getString("Id").equalsIgnoreCase("0")) {
                Id = "" + Network.MultimediaData(getContext(), "MultimediaPhotoId");
            } else if (bundle.getString("Id").equalsIgnoreCase("1")) {
                Id = "" + Network.MultimediaData(getContext(), "MultimediaVideoId");
            } else if (bundle.getString("Id").equalsIgnoreCase("2")) {
                Id = "" + Network.MultimediaData(getContext(), "MultimediaPodcastId");
            }
            Type = "" + Network.MultimediaData(getContext(), "MultimediaType");

        }

        nestedSV = rootView.findViewById(R.id.nestedSV);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);


        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());
        try {
            date1 = sdf.parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (!EndPage) {
//                        System.out.println("end");
                        page++;
                        getData(Id);
                    }
                }
            }
        });

        return rootView;

    }

    public void getData(String Id) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            String FEED_URL = "" + Network.DomainUrl(getContext()) + "" + Network.DomainService(getContext()) + "" + Network.DomainServicePage(getContext());

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("device", "android");
            jsonParam.put("api_key", "hindu@9*M");
            jsonParam.put("id", "" + Id);
            jsonParam.put("type", "" + Type);
            jsonParam.put("lut", 0);
            jsonParam.put("page", page);

//            System.out.println("" + jsonParam);
            new WebServicePost(getContext(), FEED_URL, jsonParam, new OnTaskDoneListener() {
                @Override
                public void onTaskDone(String responseData) {
                    parseResult(responseData);
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String responseData) {
                    System.out.println("Error! " + responseData);
                }
            }).execute();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }


    }


    private void parseResult(String responseData) {
        System.out.println("" + responseData);

        progressBar.setVisibility(View.GONE);
        MultiMediaModel item;
        try {
            JSONObject obj = new JSONObject(responseData);

            String ResponseStatus = String.valueOf(obj.getJSONObject("data").getInt("s"));
            if (ResponseStatus.equalsIgnoreCase("1")) {
                JSONArray Results = obj.getJSONObject("data").getJSONArray("article"); // notice that `"Results": [...]`
//                System.out.println("length " + Results.length());
                if (Results.length() > 0) {
                    if (page == 1) {
                        tv_no_data_found.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < Results.length(); i++) {
                        JSONObject article_inside = Results.getJSONObject(i);
                        item = new MultiMediaModel();
                        item.setAid(article_inside.getInt("aid"));
                        item.setTi(article_inside.getString("ti"));
                        item.setAu(article_inside.getString("au"));
                        item.setDe(article_inside.getString("de"));
                        item.setDe(article_inside.getString("de"));
                        item.setLe(article_inside.getString("le"));
                        item.setYoutubeVideoId(article_inside.getString("youtube_video_id"));
                        item.setAudioLink(article_inside.getString("audioLink"));
                        item.setIm_thumbnail(article_inside.getString("im_thumbnail"));
                        item.setSname(article_inside.getString("sname"));
                        try {
                            item.setArticleType(article_inside.getString("articleType"));
                        } catch (JSONException e) {
                        }
                        String location = "";
                        try {
                            location = article_inside.getString("location");
                        } catch (JSONException e) {
                        }
                        try {
                            item.setLocation(article_inside.getString("location"));
                        } catch (JSONException e) {
                            item.setLocation("");
                        }
                        try {
                            date2 = sdf.parse(article_inside.getString("od"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String datetime = printDifference(date2, date1);
                        item.setOd1(location + " " + datetime);
                        item.setOd("" + article_inside.getString("od"));
                        item.setPd("" + article_inside.getString("pd"));

                        JSONArray ResultsME = null;
                        try {
                            ArrayList<MediaInnerModel> mediaInnerModels;
                            mediaInnerModels = new ArrayList<>();
                            MediaInnerModel mediaInnerModel;
                            ResultsME = article_inside.getJSONArray("me");
                            if (ResultsME.length() > 0) {
                                for (int j = 0; j < ResultsME.length(); j++) {
                                    mediaInnerModel = new MediaInnerModel();
                                    JSONObject article_inside_me = ResultsME.getJSONObject(0);
                                    mediaInnerModel.setIm(article_inside_me.getString("im"));
                                    mediaInnerModel.setCa(article_inside_me.getString("ca"));
                                    try {
                                        mediaInnerModel.setIm_v2(article_inside_me.getString("im_v2"));
                                    } catch (JSONException e) {
                                        System.out.println("" + e.getMessage());
                                        item.setIm("");
                                    }
                                    mediaInnerModels.add(mediaInnerModel);
                                }
                            } else {

                            }
                            item.setMediaInnerModels(mediaInnerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        articleModels.add(item);
                    }
                    if (page == 1) {
                        imageAdapter = new MultiMediaAdapter(getContext(), getActivity(), articleModels);
                        recyclerView.setAdapter(imageAdapter);
                    }
                    imageAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        recyclerView.setVisibility(View.GONE);
                        tv_no_data_found.setVisibility(View.VISIBLE);
                        EndPage = true;
                    } else {
                        Toast.makeText(getContext(), "No more data found", Toast.LENGTH_SHORT).show();
                        EndPage = true;
                    }
                }
            } else {
                if (page == 1) {
                    recyclerView.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                } else {
                    EndPage = true;
                    Toast.makeText(getContext(), "No more data found", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays == 0 && elapsedHours == 0)
            return "" + elapsedMinutes + " min ago";
        else if (elapsedDays == 0)
            return "" + elapsedHours + " hrs ago";
        else
            return "" + elapsedDays + " day ago";
    }

}
