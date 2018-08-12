package com.mark.moveandswipervitem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
    private static final int ID_EXTERNAL = 1;
    private static final int ID_INTERNAL = 2;
    private static final int STORAGE_REQUEST_CODE = 100;
    ItemTouchHelper mItemTouchHelper;
    RecyclerView mRecyclerView;
    ItemAdapter mAdapter;
    List<ImageModel> mModels;

    /**
     * 拖拽滑动回调
     */
    private ItemTouchHelper.Callback mCallBack = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int moveFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP; //可上下移动
            int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //可左右滑动
            return makeMovementFlags(moveFlag, swipeFlag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int dragPosition = viewHolder.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();
            Collections.swap(mModels,dragPosition,targetPosition);
            mAdapter.notifyItemMoved(dragPosition,targetPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.notifyItemRemoved(position);
            mModels.remove(position);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check();
        init();
    }


    /***
     * 权限检查
     */
    void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_REQUEST_CODE
                );
                Log.e(TAG,"check 1");
            } else {
                getSupportLoaderManager().initLoader(ID_EXTERNAL, null, mLoaderCallbacks);
                Log.e(TAG,"check 2");
            }
        } else {
            Log.e(TAG,"check 3");
            getSupportLoaderManager().initLoader(ID_EXTERNAL, null, mLoaderCallbacks);
        }

    }

    void init() {

        mItemTouchHelper = new ItemTouchHelper(mCallBack);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this
                ,GridLayoutManager.VERTICAL,1,false));

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 50;
                outRect.right = 50;
                outRect.left = 50;
            }
        });
        mModels = new ArrayList<>();

        mAdapter = new ItemAdapter(this, mModels);

        mRecyclerView.setAdapter(mAdapter);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    final static String[] IMAGE_COLUMNS = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.TITLE

    };
    /***
     * LoaderManager 回调 获取手机外置存储所有图片
     */
    LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<Cursor>() {


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Loader loader;
            String selection;
            String[] selectArgs;
            switch (id) {
                case ID_EXTERNAL:
                    selection = "" + 1;
                    selectArgs = new String[]{
                    };
                    loader = new CursorLoader(
                            MainActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_COLUMNS,
                            selection,
                            selectArgs,
                            ""
                    );
                    return loader;

                case ID_INTERNAL:
                    selection = "" + 1;
                    selectArgs = new String[]{
                    };
                    loader = new CursorLoader(
                            MainActivity.this,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                            IMAGE_COLUMNS,
                            selection,
                            selectArgs,
                            ""
                    );
                    return loader;

            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader loader, Cursor data) {
            Log.e(TAG,"onLoadFinished 1");
            if (loader != null && data != null) {
                Log.e(TAG,"onLoadFinished 2");
                while (data.moveToNext()) {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_COLUMNS[1]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_COLUMNS[2]));
                    String title = data.getString(data.getColumnIndexOrThrow(IMAGE_COLUMNS[3]));
                    ImageModel model = new ImageModel(path, name, title);
                    mModels.add(model);
                    Log.e(TAG,"onLoadFinished 3");
                }
                Log.e(TAG,"onLoadFinished 4");
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {
            loader.reset();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSupportLoaderManager().initLoader(ID_EXTERNAL,null,mLoaderCallbacks);
            }
        }
    }
}
