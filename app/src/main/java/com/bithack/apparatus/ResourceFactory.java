package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ResourceFactory {
    public static final int STORE_EXTERNAL = 1;
    public static final int STORE_INTERNAL = 0;
    public static final int STORE_LEVEL = 2;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_LEVEL = 1;
    public static final int TYPE_SOUND = 2;
    public static final String external_root = "Android/data/com.bithack.superslimeblob/";
    public static final String internal_root = "data/";

    public static class Adapter {
    }

    public static class Collection {
        public String[] categories;
        public ArrayList<Resource>[] resources;
    }

    public static void initialize() {
    }

    public static Collection find_by_categories(String[] cats) {
        Collection c = new Collection();
        c.categories = cats;
        c.resources = new ArrayList[cats.length];
        int x = 0;
        for (String cat : cats) {
            c.resources[x] = new ArrayList<>();
            FileHandle dir = Gdx.files.external(external_root + cat);
            if (dir.exists()) {
                FileHandle[] list = dir.list();
                for (FileHandle f : list) {
                    c.resources[x].add(new Resource(1, String.valueOf(cat) + '/' + f.name()));
                }
            }
            FileHandle dir2 = Gdx.files.internal(internal_root + cat);
            if (dir2.exists()) {
                FileHandle[] list2 = dir2.list();
                for (FileHandle f2 : list2) {
                    c.resources[x].add(new Resource(0, String.valueOf(cat) + '/' + f2.name()));
                }
            }
            x++;
        }
        return c;
    }

    public static class Resource {
        public final String file;
        private final FileHandle handle;
        public final String name;
        public final int store;

        public Resource(int store, String filename) {
            switch (store) {
                case 0:
                    this.handle = Gdx.files.internal(ResourceFactory.internal_root + filename);
                    break;
                case 1:
                    this.handle = Gdx.files.external(ResourceFactory.external_root + filename);
                    break;
                default:
                    this.handle = Gdx.files.internal(ResourceFactory.internal_root + filename);
                    break;
            }
            this.store = store;
            this.file = filename;
            this.name = this.handle.nameWithoutExtension();
        }

        public FileHandle get_handle() {
            return this.handle;
        }

        public String get_path() {
            return this.handle.path();
        }
    }
}
