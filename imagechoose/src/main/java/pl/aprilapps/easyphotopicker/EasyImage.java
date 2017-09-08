package pl.aprilapps.easyphotopicker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacek Kwiecień on 16.10.2015.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "ResultOfMethodCallIgnored"})
public class EasyImage implements EasyImageConfig {

    private static final boolean SHOW_GALLERY_IN_CHOOSER = false;
    private static final String KEY_PHOTO_URI = "pl.aprilapps.easyphotopicker.photo_uri";
    private static final String KEY_LAST_CAMERA_PHOTO = "pl.aprilapps.easyphotopicker.last_photo";
    private static final String KEY_TYPE = "pl.aprilapps.easyphotopicker.type";

    private static Uri createCameraPictureFile(@NonNull Context context) throws IOException {
        File imagePath = EasyImageFiles.getCameraPicturesLocation(context);
        String packageName = context.getApplicationContext().getPackageName();
        String authority = packageName + ".easyphotopicker.fileprovider";
        Uri uri = FileProvider.getUriForFile(context, authority, imagePath);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_PHOTO_URI, uri.toString());
        editor.putString(KEY_LAST_CAMERA_PHOTO, imagePath.toString());
        editor.apply();
        return uri;
    }

    @NonNull
    private static Intent createDocumentsIntent(Context context, int type) {
        storeType(context, type);
          Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
         String[] mimetypes = {"image*//*","application/msword", "application/pdf", "text/plain", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
      intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
      /*  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");*/
        return intent;
    }

    @NonNull
    private static Intent createGalleryIntent(Context context, int type, boolean allowMultiple) {
        storeType(context, type);
        Intent intent = plainGalleryPickerIntent();
        if (Build.VERSION.SDK_INT >= 18) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
        }
        return intent;
    }

    @NonNull
    private static Intent createCameraIntent(@NonNull Context context, int type) {
        storeType(context, type);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri capturedImageUri = createCameraPictureFile(context);
            //We have to explicitly grant the write permission since Intent.setFlag works only on API Level >=20
            grantWritePermission(context, intent, capturedImageUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return intent;
    }

    private static void revokeWritePermission(@NonNull Context context, Uri uri) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private static void grantWritePermission(@NonNull Context context, Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private static Intent createChooserIntent(@NonNull Context context, String chooserTitle, int type) throws IOException {
        return createChooserIntent(context, chooserTitle, SHOW_GALLERY_IN_CHOOSER, type);
    }

    private static Intent createChooserIntent(@NonNull Context context, String chooserTitle, boolean showGallery, int type) throws IOException {
        storeType(context, type);

        Uri outputFileUri = createCameraPictureFile(context);
        List<Intent> cameraIntents = new ArrayList<>();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> camList = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : camList) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            grantWritePermission(context, intent, outputFileUri);
            cameraIntents.add(intent);
        }
        Intent galleryIntent;

        if (showGallery) {
            galleryIntent = createGalleryIntent(context, type, true);
        } else {
            galleryIntent = createDocumentsIntent(context, type);
        }

        Intent chooserIntent = Intent.createChooser(galleryIntent, chooserTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        return chooserIntent;
    }

    private static void storeType(Context context, int type) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_TYPE, type).commit();
    }

    private static int restoreType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TYPE, 0);
    }

    public static void openChooserWithDocuments(@NonNull Activity activity, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(activity, chooserTitle, type);
            activity.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(@NonNull Fragment fragment, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(fragment.getActivity(), chooserTitle, type);
            fragment.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(@NonNull android.app.Fragment fragment, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(fragment.getActivity(), chooserTitle, type);
            fragment.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(@NonNull Activity activity, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(activity, chooserTitle, true, type);
            activity.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(@NonNull Fragment fragment, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(fragment.getActivity(), chooserTitle, true, type);
            fragment.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openChooserWithGallery(@NonNull android.app.Fragment fragment, String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(fragment.getActivity(), chooserTitle, true, type);
            fragment.startActivityForResult(intent, REQ_SOURCE_CHOOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDocuments(@NonNull Activity activity, int type) {
        Intent intent = createDocumentsIntent(activity, type);
        activity.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(@NonNull Fragment fragment, int type) {
        Intent intent = createDocumentsIntent(fragment.getContext(), type);
        fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(@NonNull android.app.Fragment fragment, int type) {
        Intent intent = createDocumentsIntent(fragment.getActivity(), type);
        fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_DOCUMENTS);
    }

    /**
     * Opens default galery or a available galleries picker if there is no default
     *
     * @param type          Custom type of your choice, which will be returned with the images
     * @param allowMultiple Whether multiple images picking should be available. CAUTION - works only for API 18+
     */
    public static void openGallery(@NonNull Activity activity, int type, boolean allowMultiple) {
        Intent intent = createGalleryIntent(activity, type, allowMultiple);
        activity.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY);
    }

    /**
     * Opens default galery or a available galleries picker if there is no default
     *
     * @param type          Custom type of your choice, which will be returned with the images
     * @param allowMultiple Whether multiple images picking should be available. CAUTION - works only for API 18+
     */
    public static void openGallery(@NonNull Fragment fragment, int type, boolean allowMultiple) {
        Intent intent = createGalleryIntent(fragment.getContext(), type, allowMultiple);
        fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY);
    }

    /**
     * Opens default galery or a available galleries picker if there is no default
     *
     * @param type          Custom type of your choice, which will be returned with the images
     * @param allowMultiple Whether multiple images picking should be available. CAUTION - works only for API 18+
     */
    public static void openGallery(@NonNull android.app.Fragment fragment, int type, boolean allowMultiple) {
        Intent intent = createGalleryIntent(fragment.getActivity(), type, allowMultiple);
        fragment.startActivityForResult(intent, REQ_PICK_PICTURE_FROM_GALLERY);
    }

    public static void openCamera(@NonNull Activity activity, int type) {
        Intent intent = createCameraIntent(activity, type);
        activity.startActivityForResult(intent, REQ_TAKE_PICTURE);
    }

    public static void openCamera(@NonNull Fragment fragment, int type) {
        Intent intent = createCameraIntent(fragment.getActivity(), type);
        fragment.startActivityForResult(intent, REQ_TAKE_PICTURE);
    }

    public static void openCamera(@NonNull android.app.Fragment fragment, int type) {
        Intent intent = createCameraIntent(fragment.getActivity(), type);
        fragment.startActivityForResult(intent, REQ_TAKE_PICTURE);
    }

    @Nullable
    private static File takenCameraPicture(Context context) throws IOException, URISyntaxException {
        String lastCameraPhoto = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (lastCameraPhoto != null) {
            return new File(lastCameraPhoto);
        } else {
            return null;
        }
    }

    public static void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull Activity activity, @NonNull Callbacks callbacks) {
        if (requestCode == EasyImageConfig.REQ_SOURCE_CHOOSER || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY || requestCode == EasyImageConfig.REQ_TAKE_PICTURE || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
                    onPictureReturnedFromDocuments(data, activity, callbacks);
                } else if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY) {
                    onPictureReturnedFromGallery(data, activity, callbacks);
                } else if (requestCode == EasyImageConfig.REQ_TAKE_PICTURE) {
                    onPictureReturnedFromCamera(activity, callbacks);
                } else if (data == null || data.getData() == null) {
                    onPictureReturnedFromCamera(activity, callbacks);
                } else {
                    onPictureReturnedFromDocuments(data, activity, callbacks);
                }
            } else {
                if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
                    callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity));
                } else if (requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY) {
                    callbacks.onCanceled(ImageSource.GALLERY, restoreType(activity));
                } else if (requestCode == EasyImageConfig.REQ_TAKE_PICTURE) {
                    callbacks.onCanceled(ImageSource.CAMERA, restoreType(activity));
                } else if (data == null || data.getData() == null) {
                    callbacks.onCanceled(ImageSource.CAMERA, restoreType(activity));
                } else {
                    callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity));
                }
            }
        }
    }

    public static boolean willHandleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EasyImageConfig.REQ_SOURCE_CHOOSER || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY || requestCode == EasyImageConfig.REQ_TAKE_PICTURE || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            return true;
        }
        return false;
    }

    @NonNull
    private static Intent plainGalleryPickerIntent() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static boolean canDeviceHandleGallery(@NonNull Context context) {
        return plainGalleryPickerIntent().resolveActivity(context.getPackageManager()) != null;
    }

    /**
     * @param context context
     * @return File containing lastly taken (using camera) photo. Returns null if there was no photo taken or it doesn't exist anymore.
     */
    public static File lastlyTakenButCanceledPhoto(Context context) {
        String filePath = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (filePath == null) return null;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    private static void onPictureReturnedFromDocuments(@NonNull Intent data, Activity activity, @NonNull Callbacks callbacks) {
        try {
            Uri photoPath = data.getData();
            File photoFile = EasyImageFiles.pickedExistingPicture(activity, photoPath);
            List<File> files = new ArrayList<>();
            files.add(photoFile);
            callbacks.onImagesPicked(files, ImageSource.DOCUMENTS, restoreType(activity));
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.DOCUMENTS, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromGallery(@NonNull Intent data, Activity activity, @NonNull Callbacks callbacks) {
        try {
            ClipData clipData = data.getClipData();
            List<File> files = new ArrayList<>();
            if (clipData == null)
            {
                Uri uri = data.getData ();
                File file = EasyImageFiles.pickedExistingPicture (activity, uri);
                files.add (file);
            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    File file = EasyImageFiles.pickedExistingPicture(activity, uri);
                    files.add(file);
                }
            }

            callbacks.onImagesPicked(files, ImageSource.GALLERY, restoreType(activity));
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.GALLERY, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromCamera(@NonNull Activity activity, @NonNull Callbacks callbacks) {
        try {

            String lastImageUri = PreferenceManager.getDefaultSharedPreferences(activity).getString(KEY_PHOTO_URI, null);
            if (!TextUtils.isEmpty(lastImageUri)) {
                revokeWritePermission(activity, Uri.parse(lastImageUri));
            }

            File photoFile = EasyImage.takenCameraPicture(activity);
            List<File> files = new ArrayList<>();
            files.add(photoFile);

            if (photoFile == null) {
                Exception e = new IllegalStateException("Unable to get the picture returned from camera");
                callbacks.onImagePickerError(e, ImageSource.CAMERA, restoreType(activity));
            } else {
                callbacks.onImagesPicked(files, ImageSource.CAMERA, restoreType(activity));
            }

            PreferenceManager.getDefaultSharedPreferences(activity)
                    .edit()
                    .remove(KEY_LAST_CAMERA_PHOTO)
                    .remove(KEY_PHOTO_URI)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.CAMERA, restoreType(activity));
        }
    }

    public static void clearPublicTemp(Context context) {
        List<File> tempFiles = new ArrayList<>();
        File[] files = EasyImageFiles.publicTempDir(context).listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * Method to clear configuration. Would likely be used in onDestroy(), onDestroyView()...
     *
     * @param context context
     */
    public static void clearConfiguration(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .remove(BundleKeys.FOLDER_NAME)
                .remove(BundleKeys.FOLDER_LOCATION)
                .remove(BundleKeys.PUBLIC_TEMP)
                .apply();
    }

    @NonNull
    public static Configuration configuration(Context context) {
        return new Configuration(context);
    }


    public enum ImageSource {
        GALLERY, DOCUMENTS, CAMERA
    }

    public interface Callbacks {
        void onImagePickerError(Exception e, ImageSource source, int type);

        void onImagesPicked(List<File> imageFiles, ImageSource source, int type);

        void onCanceled(ImageSource source, int type);
    }

    public static class Configuration {
        private Context context;

        private Configuration(Context context) {
            this.context = context;
        }

        @NonNull
        public Configuration setImagesFolderName(String folderName) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit().putString(BundleKeys.FOLDER_NAME, folderName)
                    .apply();
            return this;
        }

        @NonNull
        public Configuration saveInRootPicturesDirectory() {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(BundleKeys.FOLDER_LOCATION, EasyImageFiles.publicRootDir(context).toString())
                    .apply();
            return this;
        }

        @NonNull
        public Configuration saveInAppExternalFilesDir() {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(BundleKeys.FOLDER_LOCATION, EasyImageFiles.publicAppExternalDir(context).toString())
                    .apply();
            return this;
        }


        /**
         * Use this method if you want your picked gallery or documents pictures to be duplicated into public, other apps accessible, directory.
         * You'll have to take care of removing that file on your own after you're done with it. Use EasyImage.clearPublicTemp() method for that.
         * If you don't delete them they could show up in user galleries.
         *
         * @return modified Configuration object
         */
        @NonNull
        public Configuration setCopyExistingPicturesToPublicLocation(boolean copyToPublicLocation) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(BundleKeys.PUBLIC_TEMP, copyToPublicLocation)
                    .commit();
            return this;
        }
    }
}