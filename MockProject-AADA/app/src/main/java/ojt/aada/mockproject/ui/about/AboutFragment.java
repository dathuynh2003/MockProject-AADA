package ojt.aada.mockproject.ui.about;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ojt.aada.mockproject.R;

public class AboutFragment extends Fragment {
    private static String ABOUT_URL = "https://www.themoviedb.org/about/our-history";
    private WebView webView;


    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        webView = view.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient()); // Open links in the WebView itself
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(ABOUT_URL);
        return view;
    }
}