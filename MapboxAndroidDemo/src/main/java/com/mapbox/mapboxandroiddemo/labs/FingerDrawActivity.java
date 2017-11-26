package com.mapbox.mapboxandroiddemo.labs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class FingerDrawActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

  private MapView mapView;
  private MapboxMap mapboxMap;
  private GeoJsonSource sourceForSelectedPolygonArea;
  private GeoJsonSource sourceForCircleTouchPoints;
  private FillLayer selectedAreaFillLayerPolygon;
  private CircleLayer circleLayerOfTouchPoints;

  private FeatureCollection circleFeatureCollection;

  private List<Feature> circleFeatureList;

  private int anchorPointNum;

  private static final String CIRCLE_LAYER_GEOJSON_SOURCE_ID = "selected-points-for-circle-geojson-id";
  private static final String CIRCLE_LAYER_ID = "selected-points-for-circle-source-id";
  private static final String SELECTED_SOURCE_LAYER_ID = "selected-area-source-id";
  private static final String SELECTED_SOURCE_GEOJSON_ID = "selected-area-source-id";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_finger_draw);

    circleFeatureList = new ArrayList<>();
    circleFeatureCollection = FeatureCollection.fromFeatures(circleFeatureList);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {

    FingerDrawActivity.this.mapboxMap = mapboxMap;
    setUpCircleSourceAndLayer();
    setUpPolygonSourceAndLayer();
  }

  private void setUpPolygonSourceAndLayer() {
    anchorPointNum = 0;
    sourceForSelectedPolygonArea = new GeoJsonSource(SELECTED_SOURCE_LAYER_ID);
    mapboxMap.addSource(sourceForSelectedPolygonArea);
    selectedAreaFillLayerPolygon = new FillLayer(SELECTED_SOURCE_LAYER_ID, SELECTED_SOURCE_GEOJSON_ID);
  }

  private void setUpCircleSourceAndLayer() {
    sourceForCircleTouchPoints = new GeoJsonSource(CIRCLE_LAYER_GEOJSON_SOURCE_ID);
    mapboxMap.addSource(sourceForCircleTouchPoints);
    circleLayerOfTouchPoints = new CircleLayer(CIRCLE_LAYER_ID, CIRCLE_LAYER_GEOJSON_SOURCE_ID);
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    anchorPointNum++;
    addClickPointAsPolygonAnchor(point);
  }

  private void addClickPointAsPolygonAnchor(LatLng point) {
    if (anchorPointNum <= 4) {
      if (sourceForCircleTouchPoints != null && circleFeatureCollection != null) {
        circleFeatureCollection.getFeatures().add(circleFeatureCollection.getFeatures().size() + 1,
          Feature.fromGeometry(Point.fromCoordinates(Position.fromCoordinates(point.getLongitude(),
            point.getLatitude()))));
        sourceForCircleTouchPoints.setGeoJson(circleFeatureCollection);
      }
    } else {
      circleFeatureCollection.getFeatures().clear();
      sourceForCircleTouchPoints.setGeoJson(circleFeatureCollection);
      mapboxMap.clear();
      anchorPointNum = 0;
    }
  }

  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}