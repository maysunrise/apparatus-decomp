package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.bithack.apparatus.ContactHandler;
import com.bithack.apparatus.ObjectManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.Pipeline;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.Bar;
import com.bithack.apparatus.objects.BaseMotor;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.BaseRope;
import com.bithack.apparatus.objects.BaseRopeEnd;
import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Bucket;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.Cable;
import com.bithack.apparatus.objects.CableEnd;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.DamperEnd;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Hinge;
import com.bithack.apparatus.objects.Hub;
import com.bithack.apparatus.objects.Knob;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.MetalBar;
import com.bithack.apparatus.objects.MetalCorner;
import com.bithack.apparatus.objects.MetalWheel;
import com.bithack.apparatus.objects.Mine;
import com.bithack.apparatus.objects.Panel;
import com.bithack.apparatus.objects.PanelCable;
import com.bithack.apparatus.objects.PanelCableEnd;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.RopeEnd;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Weight;
import com.bithack.apparatus.objects.Wheel;
import com.bithack.apparatus.ui.HorizontalSliderWidget;
import com.bithack.apparatus.ui.Widget;
import com.bithack.apparatus.ui.WidgetManager;
import com.bithack.apparatus.ui.WidgetValueCallback;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/* loaded from: classes.dex */
public class Game extends Screen implements InputProcessor, WidgetValueCallback {
    static final int HINGE_HAMMER = 1;
    static final int HINGE_WRENCH = 0;
    static final int MODE_DEFAULT = 0;
    static final int MODE_GRAB = 1;
    static final int MODE_PAUSE = 4;
    public static final int MODE_PLAY = 3;
    static final int STATE_FAIL = 3;
    static final int STATE_FINISHED = 2;
    static final int STATE_PAUSED = 1;
    static final int STATE_PLAYING = 0;
    private static final float T_EPSILON = 1.5f;
    private static final int WIDGET_CURRENT = 2;
    private static final int WIDGET_DFORCE = 10;
    private static final int WIDGET_DIR_CCW = 4;
    private static final int WIDGET_DIR_CW = 3;
    private static final int WIDGET_DSPEED = 9;
    private static final int WIDGET_ELASTICITY = 7;
    private static final int WIDGET_SIZE = 0;
    private static final int WIDGET_SIZEB = 6;
    private static final int WIDGET_THRUST = 8;
    private static final int WIDGET_VELOCITY = 5;
    private static final int WIDGET_VOLTAGE = 1;
    static Texture bgtex;
    static Texture bgtexlow;
    static Texture bloomtex;
    private static boolean has_multitouch;
    static Texture newbgtex;
    static Texture rotatetex;
    public static World world;
    private final Texture btntex;
    public PCameraHandler camera_h;
    private BodyDef drag_bd;
    private Body drag_body;
    private FixtureDef drag_fd;
    private Body ground;
    private Joint ground_joint;
    private final Texture hammertex;
    private Texture hingeselecttex;
    private int left_menu_cache_id;
    Level level;
    private Texture lvlcompletetex;
    private final SpriteCache menu_cache;
    private MouseJoint mousejoint;
    private Texture nextleveltex;
    private int object_menu_cache_id;
    private final Pipeline pipeline;
    private int rotate_dir;
    private int sandbox_categories_cache_id;
    private int special_menu_cache_id;
    private final ApparatusApp tp;
    private int undo_cache_id;
    private final HorizontalSliderWidget widget_current;
    private final HorizontalSliderWidget widget_dforce;
    private final HorizontalSliderWidget widget_dspeed;
    private final HorizontalSliderWidget widget_elasticity;
    private final HorizontalSliderWidget widget_size;
    private final HorizontalSliderWidget widget_sizeb;
    private final HorizontalSliderWidget widget_thrust;
    private final HorizontalSliderWidget widget_voltage;
    private final WidgetManager widgets;
    private final Texture wrenchtex;
    public static int level_type = 1;
    static int id_counter = 0;
    public static boolean fix_bottombar = false;
    public static boolean sandbox = false;
    public static boolean from_sandbox = false;
    public static boolean from_game = true;
    public static boolean from_community = false;
    public static boolean testing_challenge = false;
    public static boolean do_connectanims = true;
    public static boolean force_disable_shadows = false;
    public static boolean enable_shadows = true;
    public static boolean enable_multithreading = false;
    public static boolean enable_reflections = true;
    public static boolean enable_sound = false;
    public static boolean enable_music = false;
    public static boolean enable_menu = true;
    public static boolean enable_bg = true;
    public static boolean enable_bloom = true;
    public static boolean enable_hqmeshes = true;
    public static int physics_stability = 1;
    public static int camera_smoothness = 50;
    public static int camera_speed = 40;
    public static int rope_quality = 100;
    public static boolean camera_reset = true;
    public static int mode = 0;
    public static boolean draw_fps = false;
    public static int autosave_id = -1;
    public static final ArrayList<ConnectAnim> connectanims = new ArrayList<>();
    static final float[] _metal_material = {0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f, 0.2f, 1.0f, 0.8f, 0.8f, 0.8f, 1.0f, 3.0f, 0.0f, 0.0f, 0.0f};
    static final float[] _def_material = {0.5f, 0.5f, 0.5f, 1.0f, 0.75f, 0.75f, 0.75f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 10.0f, 0.0f, 0.0f, 0.0f};
    private static MouseJointDef _mjd = new MouseJointDef();
    public static final float[] light_pos = {-0.3f, 0.5f, 1.0f, 0.0f};
    static Plane yaxis = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 1.0f));
    static Plane yaxis0 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 0.0f));
    static Plane yaxis05 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 0.5f));
    static boolean tracing = false;
    private static boolean a_down = false;
    private static boolean w_down = false;
    private static boolean s_down = false;
    private static boolean d_down = false;
    static float[] nangle = new float[5];
    int level_n = 0;
    public String level_filename = null;
    private boolean modified = false;
    private boolean allow_swipe = true;
    private boolean undo_begun = false;
    private float finished_fade = 0.0f;
    private boolean do_save = false;
    private long game_start = 0;
    private boolean prevent_nail = false;
    private String msg = null;
    public boolean ready = false;
    public int state = 0;
    public int hinge_type = 0;
    private int autosave_interval = 30;
    private long last_autosave = 0;
    private boolean do_autosave = false;
    private long last_zoom = 0;
    private int open_sandbox_category = -1;
    private int open_animate_dir = 0;
    private float open_animate_time = 0.0f;
    private Matrix4 open_animate_matrix = new Matrix4();
    Panel active_panel = null;
    protected final ArrayList<Hinge> hinges = new ArrayList<>();
    Box2DDebugRenderer debug = new Box2DDebugRenderer();
    private Mesh metalmesh = null;
    private Mesh shadowmesh = null;
    int num_balls_in_goal = 0;
    int num_balls = 0;
    private int[] sandbox_category_cache_id = new int[5];
    private long time_last = 0;
    private long time_accum = 0;
    private Vector2 fpos = new Vector2();
    private Vector2 _tmp = new Vector2();
    private Vector2 _tmpv = new Vector2();
    private Vector2 _tmp2 = new Vector2();
    private Vector2 _tmp3 = new Vector2();
    private Vector2[] _touch_vec = new Vector2[3];
    private Vector2 _last_touch = new Vector2();
    private Vector2[] _last_vec = new Vector2[3];
    private float _last_dist = 0.0f;
    private final Vector3 tmp3 = new Vector3();
    private final Vector3 tmp32 = new Vector3();
    private Vector2 wrench_anim_pos = new Vector2();
    private long wrench_anim_start = 0;
    private boolean pending_follow = false;
    GrabableObject ghost_object = null;
    GrabableObject grabbed_object = null;
    GrabableObject last_grabbed = null;
    private long last_touch_time = 0;
    int level_category = -1;
    private int level_id = -1;
    private final RevoluteJointDef _rjd = new RevoluteJointDef();
    private Vector2 drag_body_target = new Vector2();
    private boolean pending_ghost = false;
    private boolean dragging_ghost = false;
    private boolean dragging = false;
    private boolean[] widget = new boolean[10];
    private boolean rotating = false;
    private Vector2 rotate_point = new Vector2(0.0f, 0.0f);
    private int num_touch_points = 0;
    final FloatBuffer light_ambient = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_dark = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_specular = BufferUtils.newFloatBuffer(4);
    final FloatBuffer light_diffuse = BufferUtils.newFloatBuffer(4);
    final FloatBuffer _light_pos = BufferUtils.newFloatBuffer(4);
    private SimpleContactHandler ingame_contact_handler = new SimpleContactHandler(this, null);
    private Vector2 grab_offs = new Vector2(0.0f, 0.0f);
    private Vector2 scroll_vec = new Vector2();
    private Plane yaxis2 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, T_EPSILON));
    private Plane yaxis3 = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, 3.0f));
    private Vector3 iresult = new Vector3();
    private boolean hingeselect = false;
    private ContactHandler.FixturePair hingepair = null;
    private int lowfpscount = 0;
    private boolean lowfpsfixed = false;
    public int background_n = -1;
    private SimulationThread sim_thread = null;
    private boolean commit_next = false;
    float accx = 0.0f;
    float accy = 0.0f;
    public boolean disable_undo = false;
    private int query_result = -1;
    private Body query_result_body = null;
    private GrabableObject query_check = null;
    private float query_result_dist2 = 0.0f;
    private Vector2 query_input_pos = null;
    private int query_input_layer = 1;
    QueryCallback query_find_object_exact = new QueryCallback() { // from class: com.bithack.apparatus.Game.1
        @Override // com.badlogic.gdx.physics.box2d.QueryCallback
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if (b.getUserData() instanceof GrabableObject) {
                if (Game.mode != 3 && ((((GrabableObject) b.getUserData()).sandbox_only || ((GrabableObject) b.getUserData()).fixed_dynamic) && !Game.sandbox)) {
                    return true;
                }
                if (Game.this.query_input_layer != 1) {
                    if (Game.this.query_input_layer != 2) {
                        if (Game.this.query_input_layer == 3 && ((GrabableObject) b.getUserData()).layer != 2) {
                            return true;
                        }
                    } else if (((GrabableObject) b.getUserData()).layer != 1) {
                        return true;
                    }
                } else if (((GrabableObject) b.getUserData()).layer >= 1) {
                    return true;
                }
                GrabableObject o = (GrabableObject) b.getUserData();
                if (fixt.isSensor() && (o instanceof Plank)) {
                    return true;
                }
                if (fixt.testPoint(Game.this.query_input_pos)) {
                    if (Game.mode == 3 && !(o instanceof Plank) && !(o instanceof Panel) && !(o instanceof Wheel) && !(o instanceof ChristmasGift) && !(o instanceof Marble) && !(o instanceof Weight) && !(o instanceof RocketEngine)) {
                        return true;
                    }
                    Game.this.query_result_body = b;
                    return false;
                }
            }
            return true;
        }
    };
    QueryCallback query_find_object = new QueryCallback() { // from class: com.bithack.apparatus.Game.2
        @Override // com.badlogic.gdx.physics.box2d.QueryCallback
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if ((b.getUserData() instanceof GrabableObject) && ((Game.mode == 3 || ((!((GrabableObject) b.getUserData()).sandbox_only && !((GrabableObject) b.getUserData()).fixed_dynamic) || Game.sandbox)) && (Game.this.query_input_layer != 1 ? Game.this.query_input_layer != 2 ? Game.this.query_input_layer != 3 || ((GrabableObject) b.getUserData()).layer == 2 : ((GrabableObject) b.getUserData()).layer == 1 : ((GrabableObject) b.getUserData()).layer < 1))) {
                GrabableObject o = (GrabableObject) b.getUserData();
                if ((!fixt.isSensor() || !(o instanceof Plank)) && (Game.mode != 3 || (o instanceof Plank) || (o instanceof Panel) || (o instanceof Wheel) || (o instanceof ChristmasGift) || (o instanceof Marble) || (o instanceof Weight) || (o instanceof RocketEngine))) {
                    if (fixt.testPoint(Game.this.query_input_pos)) {
                        Game.this.query_result_body = b;
                        Game.this.query_result_dist2 = b.getPosition().dst2(Game.this.query_input_pos);
                    } else if ((Game.this.query_result_body == null || Game.this.query_result_dist2 > b.getPosition().dst2(Game.this.query_input_pos)) && (fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.45f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.45f)))) {
                        Game.this.query_result_body = b;
                        Game.this.query_result_dist2 = b.getPosition().dst2(Game.this.query_input_pos);
                    }
                }
            }
            return true;
        }
    };
    QueryCallback query_check_drag = new QueryCallback() { // from class: com.bithack.apparatus.Game.3
        @Override // com.badlogic.gdx.physics.box2d.QueryCallback
        public boolean reportFixture(Fixture fixt) {
            Body b = fixt.getBody();
            if (b.getUserData() == Game.this.query_check) {
                if (fixt.testPoint(Game.this.query_input_pos) || fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.9f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.9f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(-0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.45f, 0.0f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, -0.45f)) || fixt.testPoint(Game.this.query_input_pos.cpy().add(0.0f, 0.45f))) {
                    Game.this.query_result_body = b;
                    return true;
                }
                return true;
            }
            return true;
        }
    };
    ContactFilter falsefilter = new ContactFilter() { // from class: com.bithack.apparatus.Game.4
        @Override // com.badlogic.gdx.physics.box2d.ContactFilter
        public boolean shouldCollide(Fixture arg0, Fixture arg1) {
            return false;
        }
    };
    public final ObjectManager om = new ObjectManager();
    public final UndoManager um = new UndoManager(this);
    ContactHandler contact_handler = new ContactHandler(this);
    Vector3 lightdir = new Vector3(light_pos);

    public Game(ApparatusApp tp) {
        this.tp = tp;
        this.lightdir.mul(-1.0f);
        this.lightdir.nor();
        for (int x = 0; x < 3; x++) {
            this._touch_vec[x] = new Vector2();
        }
        for (int x2 = 0; x2 < 3; x2++) {
            this._last_vec[x2] = new Vector2();
        }
        world = new World(new Vector2(0.0f, -10.0f), true);
        world.setContinuousPhysics(false);
        world.setAutoClearForces(true);
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
        this.light_dark.put(new float[]{0.35f, 0.35f, 0.35f, 1.0f});
        this.light_dark.rewind();
        this.light_ambient.put(new float[]{0.6f, 0.6f, 0.6f, 1.0f});
        this.light_ambient.rewind();
        this.light_diffuse.put(new float[]{0.9f, 0.9f, 0.9f, 1.0f});
        this.light_diffuse.rewind();
        this.light_specular.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        this.light_specular.rewind();
        Vector3 light = new Vector3(light_pos);
        light.nor();
        this._light_pos.put(new float[]{light.x, light.y, light.z, 0.0f});
        this._light_pos.rewind();
        this.drag_bd = new BodyDef();
        this.drag_bd.type = BodyDef.BodyType.DynamicBody;
        this.drag_fd = new FixtureDef();
        this.drag_fd.shape = new CircleShape();
        ((CircleShape) this.drag_fd.shape).setRadius(1.0f);
        this.pipeline = new Pipeline();
        this.camera_h = new PCameraHandler(G.p_cam);
        this.btntex = TextureFactory.load_unfiltered("data/btns.png");
        this.wrenchtex = TextureFactory.load("data/wrench.png");
        this.hammertex = TextureFactory.load("data/hammer.png");
        Plank._init();
        Cable._init();
        Weight._init();
        Marble._init();
        Hinge._init();
        BaseMotor._init();
        Wheel._init();
        Bar._init();
        MetalBar._init();
        Rope._init();
        Bucket._init();
        RocketEngine._init();
        IlluminationManager.init(this);
        this.menu_cache = new SpriteCache();
        generate_caches();
        this.widgets = new WidgetManager("uicontrols.png", this);
        WidgetManager widgetManager = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget = new HorizontalSliderWidget(0, 180, 1.0f);
        this.widget_size = horizontalSliderWidget;
        widgetManager.add_widget(horizontalSliderWidget, G.width - 320, G.height - 48);
        WidgetManager widgetManager2 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget2 = new HorizontalSliderWidget(7, 64, 1.0f);
        this.widget_elasticity = horizontalSliderWidget2;
        widgetManager2.add_widget(horizontalSliderWidget2, (G.width - 320) - 100, G.height - 48);
        WidgetManager widgetManager3 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget3 = new HorizontalSliderWidget(2, 180);
        this.widget_current = horizontalSliderWidget3;
        widgetManager3.add_widget(horizontalSliderWidget3, G.width - 256, G.height - 48);
        WidgetManager widgetManager4 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget4 = new HorizontalSliderWidget(1, 180);
        this.widget_voltage = horizontalSliderWidget4;
        widgetManager4.add_widget(horizontalSliderWidget4, ((G.width - 256) - 180) - 16, G.height - 48);
        WidgetManager widgetManager5 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget5 = new HorizontalSliderWidget(8, 180);
        this.widget_thrust = horizontalSliderWidget5;
        widgetManager5.add_widget(horizontalSliderWidget5, G.width - 320, G.height - 48);
        WidgetManager widgetManager6 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget6 = new HorizontalSliderWidget(6, 64, 2.0f);
        this.widget_sizeb = horizontalSliderWidget6;
        widgetManager6.add_widget(horizontalSliderWidget6, (G.width - 320) - 250, G.height - 48);
        WidgetManager widgetManager7 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget7 = new HorizontalSliderWidget(9, 180);
        this.widget_dspeed = horizontalSliderWidget7;
        widgetManager7.add_widget(horizontalSliderWidget7, G.width - 320, G.height - 48);
        WidgetManager widgetManager8 = this.widgets;
        HorizontalSliderWidget horizontalSliderWidget8 = new HorizontalSliderWidget(10, 180);
        this.widget_dforce = horizontalSliderWidget8;
        widgetManager8.add_widget(horizontalSliderWidget8, ((G.width - 320) - 180) - 16, G.height - 48);
        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_dspeed);
        this.widgets.disable(this.widget_dforce);
        String tmp = Settings.get("camerareset");
        camera_reset = tmp == null || tmp.equals("yes") || tmp.equals("");
        String tmp2 = Settings.get("camerasmoothness");
        camera_smoothness = (tmp2 == null || tmp2.equals("")) ? camera_smoothness : Integer.parseInt(tmp2);
        String tmp3 = Settings.get("cameraspeed");
        camera_speed = (tmp3 == null || tmp3.equals("")) ? camera_speed : Integer.parseInt(tmp3);
        newbgtex = TextureFactory.load("data/testnewbg.jpg");
        bloomtex = TextureFactory.load("data/bloom.png");
        rotatetex = TextureFactory.load("data/rotate.png");
        this.hingeselecttex = TextureFactory.load("data/hingeselect.png");
        this.nextleveltex = TextureFactory.load_unfiltered("data/nextlvl.png");
        set_bg(0);
        if (G.realwidth > 870) {
            this.lvlcompletetex = TextureFactory.load_unfiltered("data/lvlcomplete.png");
        } else {
            this.lvlcompletetex = TextureFactory.load_unfiltered("data/lvlcomplete.png");
        }
    }

    private void generate_caches() {
        this.menu_cache.setProjectionMatrix(G.cam_p.combined);
        this.menu_cache.beginCache();
        for (int x = 0; x < 5; x++) {
            this.menu_cache.add(this.btntex, G.width - ((x + 1) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, (x + 16) * 48, 48, 48, false, false);
        }
        this.sandbox_categories_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 112, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 720, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 168, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 0, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 224, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 48, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 280, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, 96, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 336, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.BUTTON_L2, Input.Keys.NUMPAD_0, 48, 48, false, false);
        this.sandbox_category_cache_id[0] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 112, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 288, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 168, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 336, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 224, 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 672, 48, 48, false, false);
        this.sandbox_category_cache_id[1] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x2 = 8; x2 < 14; x2++) {
            this.menu_cache.add(this.btntex, G.width - (((x2 + 2) - 8) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x2 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[2] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x3 = 3; x3 < 6; x3++) {
            this.menu_cache.add(this.btntex, G.width - (((x3 + 2) - 3) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x3 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[3] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, G.width - 56, -4.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 384, 48, 48, false, false);
        for (int x4 = 0; x4 < 3; x4++) {
            this.menu_cache.add(this.btntex, G.width - ((x4 + 2) * 56), 8.0f, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, x4 * 48, 48, 48, false, false);
        }
        this.sandbox_category_cache_id[4] = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, 8.0f, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 0, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 720, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 432, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 48, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((((G.height - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 768, 48, 48, false, false);
        this.menu_cache.add(this.btntex, 8.0f, (((((((((G.height - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 816, 48, 48, false, false);
        this.menu_cache.add(this.btntex, G.width - 56, G.height - 56, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 432, 48, 48, false, false);
        this.left_menu_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.setColor(Color.WHITE);
        this.menu_cache.add(this.btntex, 8.0f, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 0, 48, 48, false, false);
        this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.4f);
        this.menu_cache.add(this.btntex, 8.0f, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 0, 48, 48, false, false);
        this.menu_cache.setColor(Color.WHITE);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 48, 48, 48, false, false);
        this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.4f);
        this.menu_cache.add(this.btntex, 8.0f, (((((G.height - 48) - 8) - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, Input.Keys.NUMPAD_8, 48, 48, 48, false, false);
        this.menu_cache.setColor(Color.WHITE);
        this.undo_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        for (int x5 = 0; x5 < 2; x5++) {
            if (x5 == 0) {
                this.menu_cache.setColor(Color.WHITE);
            } else {
                this.menu_cache.setColor(1.0f, 1.0f, 1.0f, 0.2f);
            }
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 288, 48, 48, false, false);
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 240, 48, 48, false, false);
            this.menu_cache.add(this.btntex, (((G.width - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 96, 48, 48, false, false);
            if (x5 == 1) {
                this.menu_cache.setColor(Color.WHITE);
            }
            this.menu_cache.add(this.btntex, (G.width - 48) - 8, (((G.height - 48) - 8) - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 336, 48, 48, false, false);
        }
        this.object_menu_cache_id = this.menu_cache.endCache();
        this.menu_cache.beginCache();
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 528, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 576, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (((((G.width - 48) - 8) - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 624, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (((((G.width - 48) - 8) - 48) - 8) - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 672, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 864, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 912, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 8, 960, 48, 48, false, false);
        this.menu_cache.add(this.btntex, (G.width - 48) - 8, (G.height - 48) - 8, 0.0f, 0.0f, 48.0f, 48.0f, 1.0f, 1.0f, 0.0f, 56, 624, 48, 48, false, false);
        this.special_menu_cache_id = this.menu_cache.endCache();
    }

    public void create_level(int type) {
        String str;
        reset();
        this.level_n = -1;
        this.level_category = 0;
        this.level = Level.create(world);
        Level level = this.level;
        if (type == 1) {
            str = "challenge";
        } else {
            str = type == 2 ? "interactive" : "apparatus";
        }
        level.type = str;
        this.level_filename = null;
        this.level_category = 0;
        level_type = type;
        world.setContactFilter(this.contact_handler);
        load();
        this.ready = true;
    }

    public void open_solution(File file, int category, int n) {
        reset();
        ArrayList<Integer> fixed = new ArrayList<>();
        ArrayList<Integer> fixed_dynamic = new ArrayList<>();
        ArrayList<String> fixed_hinge = new ArrayList<>();
        open(category, n);
        Iterator<GrabableObject> it = this.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject o = it.next();
            if (o instanceof Rope) {
                Rope r = (Rope) o;
                if (((RopeEnd) r.g1).fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r.g1.__unique_id));
                }
                if (((RopeEnd) r.g2).fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r.g2.__unique_id));
                }
            } else if (o instanceof Damper) {
                Damper r2 = (Damper) o;
                if (r2.g1.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r2.g1.__unique_id));
                }
                if (r2.g2.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r2.g2.__unique_id));
                }
            } else {
                if (o.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(o.__unique_id));
                }
                if ((o instanceof BaseMotor) && ((BaseMotor) o).fixed) {
                    fixed.add(new Integer(o.__unique_id));
                }
            }
        }
        int bg = this.level.background;
        Iterator<Hinge> it2 = this.hinges.iterator();
        while (it2.hasNext()) {
            Hinge h = it2.next();
            fixed_hinge.add(String.valueOf(h.body1_id) + "_" + h.body2_id);
        }
        reset();
        world.setContactFilter(this.falsefilter);
        Level.version_override = Level.version;
        this.level = Level.open(world, file);
        Level.version_override = -1;
        this.level_filename = null;
        if (this.level != null) {
            this.ready = true;
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        level_type = 0;
        this.level_category = category;
        this.level.background = bg;
        load();
        level_type = 1;
        Iterator<GrabableObject> it3 = this.om.all.iterator();
        while (it3.hasNext()) {
            GrabableObject o2 = it3.next();
            if (o2 instanceof Rope) {
                Rope r3 = (Rope) o2;
                if (fixed_dynamic.contains(Integer.valueOf(((RopeEnd) r3.g1).__unique_id))) {
                    ((RopeEnd) r3.g1).fixed_dynamic = true;
                }
                if (fixed_dynamic.contains(Integer.valueOf(((RopeEnd) r3.g2).__unique_id))) {
                    ((RopeEnd) r3.g2).fixed_dynamic = true;
                }
            } else {
                if (fixed_dynamic.contains(Integer.valueOf(o2.__unique_id))) {
                    o2.fixed_dynamic = true;
                }
                if ((o2 instanceof BaseMotor) && fixed.contains(Integer.valueOf(o2.__unique_id))) {
                    ((BaseMotor) o2).fixed = true;
                }
            }
        }
        Iterator<Hinge> it4 = this.hinges.iterator();
        while (it4.hasNext()) {
            Hinge h2 = it4.next();
            if (fixed_hinge.contains(String.valueOf(h2.body1_id) + "_" + h2.body2_id)) {
                h2.fixed = true;
            } else {
                h2.fixed = false;
            }
        }
        pause_world();
        world.setContactFilter(this.contact_handler);
    }

    public void open(File file) {
        int i = 0;
        reset();
        Gdx.app.log("OPEN", "OPEN");
        this.level_n = -1;
        world.setContactFilter(this.falsefilter);
        this.level = Level.open(world, file);
        this.level_filename = null;
        this.level_category = 0;
        if (this.level != null) {
            this.ready = true;
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        if (!this.level.type.equals("apparatus")) {
            i = this.level.type.equals("interactive") ? 2 : 1;
        }
        level_type = i;
        load();
        world.setContactFilter(this.contact_handler);
    }

    public void open(String name) {
        int i = 0;
        reset();
        this.level_n = -1;
        this.level_category = 0;
        world.setContactFilter(this.falsefilter);
        this.level = Level.open(world, name);
        if (name.equals(".autosave")) {
            this.level_filename = null;
        } else {
            this.level_filename = name;
        }
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        if (!this.level.type.equals("apparatus")) {
            i = this.level.type.equals("interactive") ? 2 : 1;
        }
        level_type = i;
        load();
        world.setContactFilter(this.contact_handler);
    }

    public void open_interactive(int n) {
        reset();
        this.level_filename = null;
        String name = "1/" + n;
        if (n >= ApparatusApp.num_levels) {
            ApparatusApp.instance.open_levelmenu();
            Settings.msg(L.get("nicejob"));
        }
        try {
            this.level = Level.open_internal(world, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setContactFilter(this.falsefilter);
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            Gdx.app.log("CREATING", "CREATING");
            this.level = Level.create(world);
            this.ready = true;
        }
        level_type = 2;
        this.level_n = n;
        sandbox = false;
        load();
        world.setContactFilter(this.contact_handler);
        switch (this.level_n) {
            case 0:
                this.msg = L.get("ihelp1");
                break;
        }
    }

    public void open_autosave(int category, int n) {
        reset();
        ArrayList<Integer> fixed = new ArrayList<>();
        ArrayList<Integer> fixed_dynamic = new ArrayList<>();
        ArrayList<String> fixed_hinge = new ArrayList<>();
        open(category, n);
        Iterator<GrabableObject> it = this.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject o = it.next();
            if (o instanceof Rope) {
                Rope r = (Rope) o;
                if (((RopeEnd) r.g1).fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r.g1.__unique_id));
                }
                if (((RopeEnd) r.g2).fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r.g2.__unique_id));
                }
            } else if (o instanceof Damper) {
                Damper r2 = (Damper) o;
                if (r2.g1.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r2.g1.__unique_id));
                }
                if (r2.g2.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(r2.g2.__unique_id));
                }
            } else {
                if (o.fixed_dynamic) {
                    fixed_dynamic.add(new Integer(o.__unique_id));
                }
                if ((o instanceof BaseMotor) && ((BaseMotor) o).fixed && ((BaseMotor) o).attached_object != null) {
                    fixed.add(new Integer(o.__unique_id));
                }
            }
        }
        int bg = this.level.background;
        Iterator<Hinge> it2 = this.hinges.iterator();
        while (it2.hasNext()) {
            Hinge h = it2.next();
            fixed_hinge.add(String.valueOf(h.body1_id) + "_" + h.body2_id);
        }
        reset();
        this.level_filename = null;
        String str = String.valueOf(category) + "/" + n;
        world.setContactFilter(this.falsefilter);
        Level.version_override = Level.version;
        this.level = Level.open(world, ".autosave_" + n + (this.level_category == 2 ? "_2" : ""));
        Level.version_override = -1;
        this.level_filename = null;
        if (this.level != null) {
            this.ready = true;
        } else {
            Settings.msg(L.get("unable_to_open_level"));
            this.level = Level.create(world);
            this.ready = true;
        }
        this.level_n = n;
        this.level.background = bg;
        this.level_category = category;
        level_type = 0;
        load();
        level_type = 1;
        Iterator<GrabableObject> it3 = this.om.all.iterator();
        while (it3.hasNext()) {
            GrabableObject o2 = it3.next();
            if (o2 instanceof Rope) {
                Rope r3 = (Rope) o2;
                if (fixed_dynamic.contains(Integer.valueOf(((RopeEnd) r3.g1).__unique_id))) {
                    ((RopeEnd) r3.g1).fixed_dynamic = true;
                } else {
                    ((RopeEnd) r3.g1).fixed_dynamic = false;
                }
                if (fixed_dynamic.contains(Integer.valueOf(((RopeEnd) r3.g2).__unique_id))) {
                    ((RopeEnd) r3.g2).fixed_dynamic = true;
                } else {
                    ((RopeEnd) r3.g2).fixed_dynamic = false;
                }
            } else {
                if (fixed_dynamic.contains(Integer.valueOf(o2.__unique_id))) {
                    o2.fixed_dynamic = true;
                } else {
                    o2.fixed_dynamic = false;
                }
                if (o2 instanceof BaseMotor) {
                    if (fixed.contains(Integer.valueOf(o2.__unique_id))) {
                        ((BaseMotor) o2).fixed = true;
                        Gdx.app.log("basemotor was", "fixed");
                    } else {
                        ((BaseMotor) o2).fixed = false;
                    }
                }
            }
        }
        Iterator<Hinge> it4 = this.hinges.iterator();
        while (it4.hasNext()) {
            Hinge h2 = it4.next();
            if (fixed_hinge.contains(String.valueOf(h2.body1_id) + "_" + h2.body2_id)) {
                h2.fixed = true;
            } else {
                h2.fixed = false;
            }
        }
        world.setContactFilter(this.contact_handler);
        helpify();
        pause_world();
        System.gc();
    }

    private void helpify() {
        if (this.level_category == 0) {
            switch (this.level_n) {
                case 1:
                    if (has_multitouch) {
                        this.msg = L.get("help2_1");
                        break;
                    } else {
                        this.msg = L.get("help2_2");
                        break;
                    }
                default:
                    this.msg = L.get("help" + (this.level_n + 1));
                    if (this.msg.length() == 0) {
                        this.msg = null;
                        break;
                    } else if (this.msg.trim().equals("null")) {
                        this.msg = null;
                        break;
                    }
                    break;
            }
        }
        if (this.level_category == 2) {
            switch (this.level_n) {
                case 0:
                    this.msg = "Welcome to the christmas level pack!\nBefore attempting these levels, you should play some of the main challenge levels to learn the basics.\nDeliver the christmas gifts to santa's basket! All gifts must be placed in the basket to complete the level.\n";
                    break;
                case 1:
                    this.msg = "There is a new object in this level - a shock absorber. The shock absorber dampens forces acting upon it, and will act as a spring if compressed enough. Experiment with it!";
                    break;
            }
        }
    }

    public void open(int category, int n) {
        if (this.do_save && this.level_n != -1 && level_type == 1) {
            this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
            this.level.type = "apparatus";
            pause_world();
            this.do_save = false;
            save();
        }
        reset();
        this.level_filename = null;
        this.level_category = category;
        String name = String.valueOf(category) + "/" + n;
        if (n >= LevelMenu.num_levels) {
            ApparatusApp.instance.open_levelmenu();
            Settings.msg(L.get("nicejob"));
            return;
        }
        try {
            this.level = Level.open_internal(world, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setContactFilter(this.falsefilter);
        if (this.level != null) {
            this.ready = true;
            String[] wtf = name.split("/");
            if (wtf.length == 2) {
                this.level_category = Integer.parseInt(wtf[0]);
                this.level_id = Integer.parseInt(wtf[1]);
            }
        } else {
            this.level = Level.create(world);
            this.ready = true;
        }
        level_type = 1;
        this.level_n = n;
        sandbox = false;
        load();
        world.setContactFilter(this.contact_handler);
        helpify();
    }

    @Override // com.bithack.apparatus.Screen
    public int tick() {
        int iterations;
        if (!this.ready) {
            return 0;
        }
        if (mode != 3) {
            if (this.do_autosave && !this.hingeselect) {
                autosave();
                this.do_autosave = false;
            }
            if (sandbox && System.currentTimeMillis() - this.last_autosave > this.autosave_interval * 1000) {
                this.last_autosave = System.currentTimeMillis();
                if (this.modified) {
                    this.do_autosave = true;
                    return 1;
                }
            }
        }
        if (1 == 0) {
            if (Gdx.graphics.getFramesPerSecond() < 10) {
                this.lowfpscount++;
            } else {
                this.lowfpscount = 0;
            }
            if (this.lowfpscount > 300) {
                Settings.msg(L.get("gfxadjust"));
                rope_quality = 40;
                enable_shadows = false;
                this.lowfpsfixed = true;
                update_ropes();
            }
        }
        if (this.num_touch_points == 2 && !this.widget[0] && !this.widget[1] && !this.rotating && mode != 1 && has_multitouch) {
            float dist = this._last_vec[0].dst(this._last_vec[1]);
            float diff = dist - this._last_dist;
            if (Math.abs(diff) > 4.0f) {
                this.camera_h.camera_pos.z -= diff / 30.0f;
                this.last_zoom = System.currentTimeMillis();
            }
            this._last_dist = dist;
        }
        if (this.camera_h.camera_pos.z > 70.0f) {
            this.camera_h.camera_pos.z = 70.0f;
        } else if (this.camera_h.camera_pos.z < 10.0f) {
            this.camera_h.camera_pos.z = 10.0f;
        }
        G.p_cam.far = this.camera_h.camera_pos.z * 5.0f;
        G.p_cam.near = this.camera_h.camera_pos.z / 3.0f;
        long now = System.nanoTime();
        for (long delta = (now - this.time_last) + this.time_accum; delta >= 8000000; delta -= 8000000) {
            this.camera_h.tick(0.08f, false);
        }
        if (this.state == 0) {
            if (mode == 3) {
                SoundManager.tick();
                if (!enable_multithreading) {
                    if (this.time_accum > 100000000) {
                        this.time_accum = 20000000L;
                    }
                    long delta2 = (now - this.time_last) + this.time_accum;
                    if (delta2 > 100000000) {
                        delta2 = 40000000;
                    }
                    while (delta2 >= 8000000) {
                        if (physics_stability == 1) {
                            iterations = 10;
                        } else {
                            iterations = physics_stability == 0 ? 1 : 64;
                        }
                        world.step(0.011f, iterations, iterations);
                        Iterator<Hinge> it = this.hinges.iterator();
                        while (it.hasNext()) {
                            Hinge h = it.next();
                            h.tick();
                        }
                        Iterator<RocketEngine> it2 = this.om.rocketengines.iterator();
                        while (it2.hasNext()) {
                            RocketEngine e = it2.next();
                            e.step(0.008f);
                        }
                        if (Level.version >= 7) {
                            Iterator<Rope> it3 = this.om.ropes.iterator();
                            while (it3.hasNext()) {
                                Rope r = it3.next();
                                r.tick();
                            }
                            Iterator<PanelCable> it4 = this.om.pcables.iterator();
                            while (it4.hasNext()) {
                                PanelCable r2 = it4.next();
                                r2.tick();
                            }
                            Iterator<Cable> it5 = this.om.cables.iterator();
                            while (it5.hasNext()) {
                                Cable r3 = it5.next();
                                r3.tick();
                            }
                        }
                        delta2 -= 8000000;
                    }
                    this.time_accum = delta2;
                }
                if ((level_type == 1 || level_type == 2) && this.num_balls_in_goal == this.num_balls && this.num_balls != 0 && !from_sandbox && !sandbox) {
                    win();
                }
            } else {
                this.contact_handler.clean();
                world.step(0.001f, 160, 160);
                if (this.commit_next) {
                    this.um.commit_step();
                    this.commit_next = false;
                }
            }
        } else if (this.state == 2) {
            if (this.time_accum > 100000000) {
                this.time_accum = 20000000L;
            }
            long delta3 = (now - this.time_last) + this.time_accum;
            if (delta3 > 100000000) {
                delta3 = 40000000;
            }
            while (delta3 >= 8000000) {
                world.step(0.011f, 10, 10);
                Iterator<Hinge> it6 = this.hinges.iterator();
                while (it6.hasNext()) {
                    Hinge h2 = it6.next();
                    h2.tick();
                }
                delta3 -= 8000000;
            }
            this.time_accum = delta3;
        }
        this.time_last = now;
        if (mode != 3 || Level.version < 7) {
            Iterator<Rope> it7 = this.om.ropes.iterator();
            while (it7.hasNext()) {
                Rope r4 = it7.next();
                r4.tick();
            }
            Iterator<PanelCable> it8 = this.om.pcables.iterator();
            while (it8.hasNext()) {
                PanelCable r5 = it8.next();
                r5.tick();
            }
            Iterator<Cable> it9 = this.om.cables.iterator();
            while (it9.hasNext()) {
                Cable r6 = it9.next();
                r6.tick();
            }
        }
        if (sandbox && mode != 3) {
            Iterator<MetalBar> it10 = this.om.layer0.bars.iterator();
            while (it10.hasNext()) {
                MetalBar r7 = it10.next();
                r7.tick();
            }
            Iterator<MetalBar> it11 = this.om.layer1.bars.iterator();
            while (it11.hasNext()) {
                MetalBar r8 = it11.next();
                r8.tick();
            }
        }
        return 0;
    }

    private void win() {
        this.state = 2;
        this.finished_fade = 0.0f;
        Settings.set(String.valueOf(LevelMenu.lvl_prefix) + "0/" + this.level_n, "1");
        SoundManager.stop_all();
        SoundManager.play_lvlcomplete();
    }

    void render_scene(boolean light) {
        G.gl.glMatrixMode(GL10.GL_PROJECTION);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glLoadIdentity();
        G.gl.glDisable(3042);
        G.gl.glEnable(3553);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.p_cam.apply(G.gl);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        G.gl.glMatrixMode(5890);
        G.gl.glScalef(0.2f, 46.0f, 1.0f);
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(16384);
        if (light) {
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_specular);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_diffuse);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
        } else {
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
        }
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _def_material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _def_material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _def_material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _def_material, 12);
        if (this.ghost_object != null) {
            G.gl.glEnable(3042);
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            if (this.ghost_object instanceof BaseRope) {
                ((BaseRope) this.ghost_object).render_edges();
            } else {
                this.ghost_object.render();
            }
            G.gl.glEnable(GL10.GL_LIGHTING);
            G.gl.glEnable(3553);
            G.gl.glDisable(3042);
        }
    }

    private void render_static_shadows(Vector3 lightdir, boolean front) {
    }

    private void render_shadows(Vector3 lightdir, boolean front) {
        render_static_shadows(lightdir, front);
    }

    /* JADX WARN: Unreachable blocks removed: 21, instructions: 122 */
    private void boundscheck_camera() {
    }

    private void cull_objects() {
        ArrayList<GrabableObject> objects = this.om.all;
        int sz = objects.size();
        ArrayList<Hinge> hobjects = this.hinges;
        int hsz = this.hinges.size();
        float z = this.camera_h.camera_pos.z * T_EPSILON;
        for (int x = 0; x < sz; x++) {
            GrabableObject o = objects.get(x);
            o.culled = false;
            Vector2 pos = o.get_state().position;
            if (pos != null) {
                float dx = pos.x - this.camera_h.camera_pos.x;
                float dy = pos.y - this.camera_h.camera_pos.y;
                if (dx < (-20.0f) - z || dx > 20.0f + z || dy < (-5.0f) - z || dy > 5.0f + z) {
                    o.culled = true;
                }
            }
        }
        for (int x2 = 0; x2 < hsz; x2++) {
            Hinge o2 = hobjects.get(x2);
            o2.culled = false;
            Vector2 pos2 = o2.get_state().position;
            if (pos2 != null) {
                float dx2 = pos2.x - this.camera_h.camera_pos.x;
                float dy2 = pos2.y - this.camera_h.camera_pos.y;
                if (dx2 < (-15.0f) - z || dx2 > 15.0f + z || dy2 < (-5.0f) - z || dy2 > 5.0f + z) {
                    o2.culled = true;
                }
            }
        }
    }

    private void cull_and_swap() {
        Vector2 pos;
        ArrayList<GrabableObject> objects = this.om.all;
        int sz = objects.size();
        ArrayList<Hinge> hobjects = this.hinges;
        int hsz = this.hinges.size();
        float z = this.camera_h.camera_pos.z * T_EPSILON;
        for (int x = 0; x < sz; x++) {
            GrabableObject o = objects.get(x);
            o.culled = false;
            o.save_state();
            if (!(o instanceof BaseRope) && (pos = o.get_state().position) != null) {
                float dx = pos.x - this.camera_h.camera_pos.x;
                float dy = pos.y - this.camera_h.camera_pos.y;
                if (dx < (-15.0f) - z || dx > 15.0f + z || dy < (-5.0f) - z || dy > 5.0f + z) {
                    o.culled = true;
                }
            }
        }
        for (int x2 = 0; x2 < hsz; x2++) {
            Hinge o2 = hobjects.get(x2);
            o2.culled = false;
            if (o2.joint != null) {
                o2.save_state();
                Vector2 pos2 = o2.get_state().position;
                if (pos2 != null) {
                    float dx2 = pos2.x - this.camera_h.camera_pos.x;
                    float dy2 = pos2.y - this.camera_h.camera_pos.y;
                    if (dx2 < (-15.0f) - z || dx2 > 15.0f + z || dy2 < (-5.0f) - z || dy2 > 5.0f + z) {
                        o2.culled = true;
                    }
                }
            }
        }
    }

    @Override // com.bithack.apparatus.Screen
    public void render() {
        if (this.ready) {
            G.set_clear_color(0.3f, 0.3f, 0.3f);
            G.gl.glClearStencil(5);
            if (enable_reflections) {
                G.gl.glClear(1280);
            } else {
                G.gl.glClear(256);
            }
            if (!enable_bg) {
                G.gl.glClear(16384);
            }
            G.gl.glDisable(3042);
            G.gl.glEnable(2929);
            G.gl.glEnable(2884);
            G.gl.glCullFace(1029);
            G.gl.glDepthFunc(513);
            G.gl.glDepthMask(true);
            boundscheck_camera();
            this.camera_h.update();
            if (mode == 3 && enable_multithreading) {
                this.sim_thread.swap_state_buffers();
                cull_objects();
            } else {
                cull_and_swap();
            }
            G.gl.glEnable(16384);
            G.gl.glLightfv(16384, GL10.GL_AMBIENT, this.light_ambient);
            G.gl.glLightfv(16384, GL10.GL_SPECULAR, this.light_specular);
            G.gl.glLightfv(16384, GL10.GL_DIFFUSE, this.light_diffuse);
            G.gl.glLightfv(16384, GL10.GL_POSITION, this._light_pos);
            G.p_cam.apply(G.gl);
            IlluminationManager.render_scene(G.p_cam, this.hinges, this.om, enable_bg);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (enable_reflections) {
                IlluminationManager.render_reflections(this.hinges, this.om);
            }
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glDisable(3042);
            G.gl.glDisable(2960);
            G.gl.glDisable(2884);
            G.gl.glDisable(GL10.GL_NORMALIZE);
            G.gl.glDisable(GL10.GL_LIGHTING);
            G.gl.glCullFace(1029);
            G.gl.glDepthMask(true);
            G.p_cam.apply(G.gl);
            G.gl.glDisable(2884);
            G.gl.glMatrixMode(GL10.GL_PROJECTION);
            G.gl.glLoadMatrixf(G.p_cam.combined.val, 0);
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            G.gl.glLoadIdentity();
            if (enable_bloom) {
                IlluminationManager.render_bloom(this.camera_h, this.om);
            }
            G.gl.glDisable(2929);
            G.gl.glDepthMask(true);
            G.gl.glBlendFunc(770, 771);
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (enable_menu && connectanims.size() > 0) {
                Iterator i = connectanims.iterator();
                while (i.hasNext()) {
                    ConnectAnim x = i.next();
                    if (!x.render()) {
                        i.remove();
                    }
                }
            }
            if (mode != 3 && this.grabbed_object != null) {
                G.gl.glDisable(3553);
                G.gl.glEnable(3042);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                GrabableObject o = this.grabbed_object;
                G.gl.glTranslatef(o.get_state().position.x, o.get_state().position.y, o.layer + 1);
                G.gl.glRotatef((float) (o.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                if (!this.grabbed_object.fixed_rotation && !(this.grabbed_object instanceof Wheel)) {
                    G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    G.gl.glPushMatrix();
                    float size = 2.0f;
                    if (this.grabbed_object instanceof Bar) {
                        size = (((Bar) this.grabbed_object).size.x / 2.0f) + 2.0f;
                    }
                    MiscRenderer.draw_line(0.0f, 0.0f, size, 0.0f);
                    G.gl.glPopMatrix();
                    G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(0.5f + size, 0.0f, 0.0f);
                    G.gl.glScalef(0.5f, 0.5f, 1.0f);
                    G.gl.glEnable(3553);
                    rotatetex.bind();
                    MiscRenderer.draw_textured_box();
                    G.gl.glDisable(3553);
                    G.gl.glPopMatrix();
                }
                G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.2f);
                if (o instanceof Bar) {
                    G.gl.glScalef(((Bar) o).size.x + 0.2f, ((Bar) o).size.y + 0.2f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Wheel) {
                    G.gl.glScalef(((Wheel) o).size + 0.1f, ((Wheel) o).size + 0.1f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if ((o instanceof RopeEnd) || (o instanceof CableEnd) || (o instanceof PanelCableEnd) || (o instanceof Marble)) {
                    G.gl.glScalef(0.65f, 0.65f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if (o instanceof StaticMotor) {
                    G.gl.glScalef(0.8f, 0.8f, 1.0f);
                    MiscRenderer.draw_colored_ball();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_circle();
                } else if (o instanceof Bucket) {
                    G.gl.glScalef(5.0f, 3.0f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Battery) {
                    if (((Battery) o).size == 1) {
                        G.gl.glScalef(1.0f, 1.0f, 1.0f);
                        MiscRenderer.draw_colored_box();
                        G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                        MiscRenderer.draw_colored_square();
                    } else {
                        G.gl.glScalef(2.5f, 2.1f, 1.0f);
                        MiscRenderer.draw_colored_box();
                        G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                        MiscRenderer.draw_colored_square();
                    }
                } else if (o instanceof Weight) {
                    G.gl.glScalef(2.0f, T_EPSILON, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof DynamicMotor) {
                    G.gl.glScalef(T_EPSILON, T_EPSILON, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof RocketEngine) {
                    G.gl.glScalef(1.0f, 2.0f, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                } else if (o instanceof Panel) {
                    G.gl.glScalef(2.3f, T_EPSILON, 1.0f);
                    MiscRenderer.draw_colored_box();
                    G.gl.glColor4f(0.8f, 0.8f, 1.0f, 0.6f);
                    MiscRenderer.draw_colored_square();
                }
                G.gl.glPopMatrix();
            }
            if (this.ghost_object != null) {
                G.gl.glEnable(3042);
                G.gl.glDisable(GL10.GL_LIGHTING);
                G.gl.glDisable(3553);
                G.gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
                if (this.ghost_object instanceof BaseRope) {
                    ((BaseRope) this.ghost_object).render_edges();
                } else {
                    this.ghost_object.render();
                }
                G.gl.glDisable(3042);
            } else if (this.active_panel != null) {
                G.gl.glEnable(3042);
                G.gl.glDisable(3553);
                G.gl.glColor4f(0.0f, 1.0f, 0.0f, 0.3f);
                this.active_panel.render();
                G.gl.glDisable(3042);
            }
            G.gl.glLoadIdentity();
            G.gl.glEnable(3553);
            render_menu();
            G.batch.setProjectionMatrix(G.cam_p.combined);
            if (draw_fps) {
                G.batch.begin();
                G.font.draw(G.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0.0f, 24.0f);
                G.batch.end();
            }
            if (this.do_autosave) {
                G.batch.begin();
                G.font.draw(G.batch, "Auto-saving...", 0.0f, 24.0f);
                G.batch.end();
            } else if (from_sandbox && mode != 3) {
                G.batch.begin();
                G.font.draw(G.batch, L.get("testingchallenge"), 0.0f, 24.0f);
                G.batch.end();
            }
            if (this.pending_follow && mode == 3) {
                G.batch.begin();
                G.font.draw(G.batch, "Click on an object to follow it, or on the background to cancel.", 0.0f, 24.0f);
                this.state = 1;
                G.batch.end();
            }
            if ((!this.pending_follow && this.state != 0) || this.finished_fade > 0.0f) {
                G.gl.glEnable(3042);
                G.gl.glMatrixMode(GL10.GL_PROJECTION);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glLoadIdentity();
                G.color(0.0f, 0.0f, 0.0f, 0.7f * this.finished_fade);
                MiscRenderer.boxmesh.render(6);
                G.gl.glMatrixMode(GL10.GL_PROJECTION);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPopMatrix();
                if (this.state != 0) {
                    this.finished_fade += G.delta * 4.0f;
                    if (this.finished_fade >= 1.0f) {
                        this.finished_fade = 1.0f;
                    }
                } else if (this.state == 0) {
                    this.finished_fade -= G.delta * 4.0f;
                    if (this.finished_fade <= 0.0f) {
                        this.finished_fade = 0.0f;
                    }
                }
            }
            G.batch.begin();
            if (this.finished_fade > 0.0f) {
                G.batch.setColor(1.0f, 1.0f, 1.0f, this.finished_fade);
                G.batch.draw(this.lvlcompletetex, 0.0f, 0.0f, 0.0f, 0.0f, G.width, G.height, 1.0f, 1.0f, 0.0f, 0, 0, this.lvlcompletetex.getWidth(), this.lvlcompletetex.getHeight(), false, false);
                G.batch.setColor(Color.WHITE);
            }
            G.batch.end();
            Gdx.app.getGraphics().getType();
            Graphics.GraphicsType graphicsType = Graphics.GraphicsType.AndroidGL;
        }
    }

    private void render_menu() {
        G.cam_p.apply(G.gl);
        G.batch.setColor(Color.WHITE);
        G.batch.setProjectionMatrix(G.cam_p.combined);
        G.batch.begin();
        for (int x = 0; x < ContactHandler.num_fixture_pairs; x++) {
            ContactHandler.FixturePair fp = ContactHandler.fixture_pairs[x];
            Vector2 point = fp.get_intersection_point();
            if (point != null && (fp.a.getBody().getUserData() == this.grabbed_object || fp.b.getBody().getUserData() == this.grabbed_object)) {
                this.tmp3.set(point.x, point.y, 3.0f);
                G.p_cam.project(this.tmp3);
                this.tmp3.x *= G.width / G.realwidth;
                this.tmp3.y *= G.height / G.realheight;
                if (fp.same_layer) {
                    if (fp.a.getBody().getUserData() instanceof Plank) {
                        G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 32.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, ((fp.a == ((Plank) fp.a.getBody().getUserData()).fa ? 0 : 1) * 180) + (((float) (fp.a.getBody().getAngle() * 57.29577951308232d)) - 45.0f), 0, 0, 64, 128, false, false);
                    } else if ((fp.a.getBody().getUserData() instanceof DynamicMotor) || (fp.a.getBody().getUserData() instanceof Panel) || (fp.a.getBody().getUserData() instanceof RocketEngine) || (fp.a.getBody().getUserData() instanceof Hub) || (fp.a.getBody().getUserData() instanceof DamperEnd)) {
                        G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 32.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 64, 128, false, false);
                    }
                } else if (this.hinge_type == 1) {
                    G.batch.draw(this.hammertex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 64.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 64, 128, false, false);
                } else {
                    G.batch.draw(this.wrenchtex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 32.0f, 128.0f, 1.0f, 1.0f, 45.0f, 0, 0, 32, 128, false, false);
                }
            }
        }
        if (this.hingeselect) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 1.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.x *= G.width / G.realwidth;
            this.tmp3.y *= G.height / G.realheight;
            G.batch.draw(this.hingeselecttex, this.tmp3.x, this.tmp3.y, 0.0f, 0.0f, 128.0f, 64.0f, 1.0f, 1.0f, 0.0f, 0, 64, 128, 64, false, false);
        }
        long now = System.currentTimeMillis();
        if (now - this.wrench_anim_start < 400) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 3.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.x *= G.width / G.realwidth;
            this.tmp3.y *= G.height / G.realheight;
            G.batch.draw(this.wrenchtex, this.tmp3.x - 18.0f, this.tmp3.y - 112.0f, 14.0f, 112.0f, 32.0f, 128.0f, 1.0f, 1.0f, 45.0f - (80.0f * ((now - this.wrench_anim_start) / 400.0f)), 0, 0, 32, 128, false, false);
        }
        G.batch.end();
        G.cam_p.apply(G.gl);
        if (this.active_panel != null && mode == 3) {
            this.active_panel.widgets.render_all(G.batch);
        }
        if (enable_menu) {
            G.gl.glEnable(3042);
            G.gl.glBlendFunc(770, 771);
            this.menu_cache.begin();
            if (mode != 3) {
                if (this.msg != null) {
                    this.menu_cache.draw(this.left_menu_cache_id, 0, 2);
                } else {
                    this.menu_cache.draw(this.left_menu_cache_id, 0, 1);
                }
                this.menu_cache.draw(this.undo_cache_id, (this.um.can_undo() ? 0 : 1) + 0, 1);
                if (sandbox) {
                    if (this.open_animate_dir == -1) {
                        this.open_animate_time -= G.delta;
                        if (this.open_animate_time < 0.0f) {
                            this.open_animate_dir = 0;
                            this.open_animate_time = 0.0f;
                            this.open_sandbox_category = -1;
                        }
                    } else if (this.open_animate_dir == 1) {
                        this.open_animate_time += G.delta;
                        if (this.open_animate_time > 0.2f) {
                            this.open_animate_dir = 0;
                            this.open_animate_time = 0.2f;
                        }
                    }
                    if (this.open_animate_dir != 0) {
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.setToTranslation(0.0f, (-56.0f) * (this.open_animate_time / 0.2f), 0.0f));
                        this.menu_cache.begin();
                        this.menu_cache.draw(this.sandbox_categories_cache_id);
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.setToTranslation(0.0f, (-56.0f) + (56.0f * (this.open_animate_time / 0.2f)), 0.0f));
                        this.menu_cache.begin();
                        this.menu_cache.draw(this.sandbox_category_cache_id[this.open_sandbox_category]);
                        this.menu_cache.end();
                        this.menu_cache.setTransformMatrix(this.open_animate_matrix.idt());
                        this.menu_cache.begin();
                    } else if (this.open_sandbox_category != -1) {
                        this.menu_cache.draw(this.sandbox_category_cache_id[this.open_sandbox_category]);
                    } else {
                        this.menu_cache.draw(this.sandbox_categories_cache_id);
                    }
                }
                if (this.grabbed_object != null) {
                    if (!(this.grabbed_object instanceof Battery)) {
                        if (sandbox && (this.grabbed_object instanceof Plank)) {
                            this.menu_cache.draw(this.special_menu_cache_id, this.grabbed_object.layer + 4, 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        } else if (sandbox && (this.grabbed_object instanceof Panel)) {
                            this.menu_cache.draw(this.special_menu_cache_id, 7, 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        } else {
                            this.menu_cache.draw(this.object_menu_cache_id, (this.grabbed_object.layer == 1 ? 1 : 0) + (((this.grabbed_object.fixed_layer || !(this.level_n >= 7 || this.level_n == -1 || this.level_category == 2)) ? 1 : 0) * 4), 1);
                            this.menu_cache.draw(this.object_menu_cache_id, ((this.grabbed_object.num_hinges == 0 ? 1 : 0) * 4) + 2, 1);
                        }
                        if (this.grabbed_object instanceof BaseMotor) {
                            this.menu_cache.draw(this.special_menu_cache_id, (((BaseMotor) this.grabbed_object).dir > 0.0f ? 1 : 0) + 2, 1);
                        }
                        if (sandbox) {
                            this.menu_cache.draw(this.object_menu_cache_id, 3, 1);
                        }
                    } else {
                        this.menu_cache.draw(this.object_menu_cache_id, 3, 1);
                        this.menu_cache.draw(this.special_menu_cache_id, (((Battery) this.grabbed_object).real_on ? 0 : 1) + 0, 1);
                    }
                }
            } else {
                this.menu_cache.draw(this.left_menu_cache_id, 3, 1);
                this.menu_cache.draw(this.left_menu_cache_id, 6, 1);
                if (level_type == 2 && this.msg != null) {
                    this.menu_cache.draw(this.left_menu_cache_id, 1, 2);
                }
            }
            if (!has_multitouch) {
                this.menu_cache.draw(this.left_menu_cache_id, 4, 2);
            }
            this.menu_cache.end();
            if (mode != 3) {
                G.batch.begin();
                if (from_game) {
                    G.batch.draw(this.nextleveltex, 80.0f, G.realheight - 48, 0.0f, 0.0f, 128.0f, 32.0f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 32, false, false);
                }
                if (sandbox && this.grabbed_object != null) {
                    if (this.grabbed_object instanceof RocketEngine) {
                        G.font.draw(G.batch, "Thrust", (G.width - 256) + 70, G.height - 56);
                    } else if (this.grabbed_object instanceof Battery) {
                        G.font.draw(G.batch, L.get("current"), (G.width - 256) + 70, G.height - 56);
                        G.font.draw(G.batch, L.get("voltage"), ((G.width - 256) - 180) + 46, G.height - 56);
                        G.font.draw(G.batch, L.get("size"), (G.width - 256) - 300, G.height - 56);
                    } else if (this.grabbed_object instanceof DamperEnd) {
                        G.font.draw(G.batch, "Speed", (G.width - 320) + 70, G.height - 56);
                        G.font.draw(G.batch, "Force", ((G.width - 320) - 180) + 46, G.height - 56);
                    } else if ((this.grabbed_object instanceof Wheel) || (this.grabbed_object instanceof Plank) || (this.grabbed_object instanceof MetalBar)) {
                        G.font.draw(G.batch, L.get("size"), (G.width - 320) + 75, G.height - 56);
                    } else if (this.grabbed_object instanceof BaseRopeEnd) {
                        G.font.draw(G.batch, L.get("size"), (G.width - 320) + 75, G.height - 56);
                        if (this.grabbed_object instanceof RopeEnd) {
                            G.font.draw(G.batch, "Elasticity", (G.width - 256) - 162, G.height - 56);
                        }
                    }
                }
                G.batch.end();
                if (sandbox) {
                    this.widgets.render_all(G.batch);
                }
            }
        }
    }

    private void create_metal_cache() {
    }

    void reset() {
        release_object();
        this.msg = null;
        this.hingeselect = false;
        this.num_touch_points = 0;
        this.num_balls_in_goal = 0;
        this.num_balls = 0;
        this.state = 0;
        this.last_autosave = System.currentTimeMillis();
        world.setContactFilter(null);
        world.setContactListener(null);
        this.um.reset();
        this.om.clear();
        this.hinges.clear();
        mode = 4;
        this.grabbed_object = null;
        this.last_grabbed = null;
        this.contact_handler.reset();
        this.camera_h.camera_pos.set(0.0f, 0.0f, 0.0f);
        G.cam.position.set(0.0f, 0.0f, 0.0f);
        G.cam.update();
        Array<Joint> joints = new Array<>();
        world.getJoints(joints);
        Iterator<Joint> i = joints.iterator();
        while (i.hasNext()) {
            Joint j = i.next();
            try {
                world.destroyJoint(j);
            } catch (Exception e) {
                Gdx.app.log("EXCEPTION", "while removing joint");
            }
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        Iterator<Body> i2 = bodies.iterator();
        while (i2.hasNext()) {
            Body b = i2.next();
            if (b == null) {
                Gdx.app.log("WTFFFFFFFFFFFFFFFFFFFFFFF", "NULL?");
            } else {
                try {
                    world.destroyBody(b);
                } catch (NullPointerException e2) {
                    Gdx.app.log("null EXCEPTION", "while destroying body");
                } catch (Exception e3) {
                    Gdx.app.log("EXCEPTION", "while destroying body");
                }
            }
        }
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
    }

    private void load() {
        Vector2 pos;
        Gdx.app.log("LOAD", "LOAD");
        set_mode(4);
        this.contact_handler.reset();
        this.modified = false;
        this.lowfpsfixed = false;
        from_game = false;
        id_counter = 0;
        Vector2 avgpos = new Vector2();
        int num_pos = 0;
        BaseObject[] baseObjectArr = this.level.get_objects();
        int length = baseObjectArr.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                break;
            }
            BaseObject o = baseObjectArr[i2];
            if (o instanceof Hinge) {
                this.hinges.add((Hinge) o);
            } else if (o instanceof GrabableObject) {
                if (!(o instanceof BaseRope) && ((GrabableObject) o).body != null && (pos = ((GrabableObject) o).body.getPosition()) != null) {
                    avgpos.add(pos);
                    num_pos++;
                }
                this.om.add((GrabableObject) o);
            }
            o.set_game(this);
            if (o.__unique_id > id_counter) {
                id_counter = o.__unique_id;
            }
            i = i2 + 1;
        }
        if (num_pos != 0) {
            avgpos.x /= num_pos;
            avgpos.y /= num_pos;
        } else {
            avgpos.set(0.0f, 0.0f);
        }
        this.camera_h.camera_pos.set(avgpos.x + 7.0f, avgpos.y - 5.0f, 15.0f);
        this.camera_h.save();
        id_counter++;
        Iterator<StaticMotor> it = this.om.static_motors.iterator();
        while (it.hasNext()) {
            BaseMotor m = it.next();
            m.load(this.om.all);
        }
        Iterator<DynamicMotor> it2 = this.om.layer0.dynamicmotors.iterator();
        while (it2.hasNext()) {
            BaseMotor m2 = it2.next();
            m2.load(this.om.all);
        }
        Iterator<DynamicMotor> it3 = this.om.layer1.dynamicmotors.iterator();
        while (it3.hasNext()) {
            BaseMotor m3 = it3.next();
            m3.load(this.om.all);
        }
        Gdx.app.log("loaded motors", "ja");
        Iterator<Hinge> i3 = this.hinges.iterator();
        while (i3.hasNext()) {
            Hinge h = i3.next();
            GrabableObject o1 = null;
            GrabableObject o2 = null;
            int n1 = h.body1_id;
            int n2 = h.body2_id;
            Iterator<GrabableObject> it4 = this.om.all.iterator();
            while (it4.hasNext()) {
                GrabableObject o3 = it4.next();
                if (o3.__unique_id == n1) {
                    o1 = o3;
                } else if (o3.__unique_id == n2) {
                    o2 = o3;
                } else if (o3 instanceof Rope) {
                    Rope r = (Rope) o3;
                    if (r.g1.__unique_id == n1) {
                        o1 = r.g1;
                    }
                    if (r.g1.__unique_id == n2) {
                        o2 = r.g1;
                    }
                    if (r.g2.__unique_id == n1) {
                        o1 = r.g2;
                    }
                    if (r.g2.__unique_id == n2) {
                        o2 = r.g2;
                    }
                } else if (o3 instanceof Damper) {
                    Damper r2 = (Damper) o3;
                    if (r2.g1.__unique_id == n1) {
                        o1 = r2.g1;
                    }
                    if (r2.g1.__unique_id == n2) {
                        o2 = r2.g1;
                    }
                    if (r2.g2.__unique_id == n1) {
                        o1 = r2.g2;
                    }
                    if (r2.g2.__unique_id == n2) {
                        o2 = r2.g2;
                    }
                }
                if (o1 != null && o2 != null) {
                    break;
                }
            }
            if (o1 != null && o2 != null) {
                if (!sandbox && level_type == 1) {
                    o1.fixed_dynamic = true;
                    o2.fixed_dynamic = true;
                }
                h.setup(o1, o2, h.anchor);
            } else {
                Gdx.app.log("ERROR", "invalid hinge object ids");
                h.dispose(world);
                i3.remove();
            }
        }
        Gdx.app.log("TJATJAJTA", "TJATJA");
        this.ground = ObjectFactory.create_anchor_body(world);
        this.ground.setTransform(new Vector2(-10000.0f, 0.0f), 0.0f);
        pause_world();
        from_sandbox = false;
        this.lowfpscount = 0;
        if (from_community && this.om.layer0.controllers.size() > 0) {
            this.msg = L.get("hascontrolpanel");
        }
        MiscRenderer.update_quality();
        if (this.level_category == 2) {
            set_bg(10);
        } else {
            set_bg(this.level.background);
        }
        Gdx.app.log("LOADED", "LOADED");
    }

    private void resolve_cable_connections() {
        if (Level.version >= 3) {
            Iterator<PanelCable> it = this.om.pcables.iterator();
            while (it.hasNext()) {
                PanelCable p = it.next();
                PanelCableEnd e1 = (PanelCableEnd) p.g1;
                PanelCableEnd e2 = (PanelCableEnd) p.g2;
                Iterator<GrabableObject> it2 = this.om.all.iterator();
                while (it2.hasNext()) {
                    GrabableObject o = it2.next();
                    if (e1.saved_oid != -1 && o.__unique_id == e1.saved_oid) {
                        if (o instanceof Battery) {
                            e1.attach_to_battery((Battery) o);
                        } else if (o instanceof Panel) {
                            e1.attach_to_panel((Panel) o);
                        } else if (o instanceof Hub) {
                            e1.attach_to_hub((Hub) o);
                        } else if (o instanceof RocketEngine) {
                            e1.attach_to_rengine((RocketEngine) o);
                        } else if (o instanceof Button) {
                            e1.attach_to_button((Button) o);
                        }
                    }
                    if (e2.saved_oid != -1 && o.__unique_id == e2.saved_oid) {
                        if (o instanceof Battery) {
                            e2.attach_to_battery((Battery) o);
                        } else if (o instanceof Panel) {
                            e2.attach_to_panel((Panel) o);
                        } else if (o instanceof Hub) {
                            e2.attach_to_hub((Hub) o);
                        } else if (o instanceof RocketEngine) {
                            e2.attach_to_rengine((RocketEngine) o);
                        } else if (o instanceof Button) {
                            e2.attach_to_button((Button) o);
                        }
                    }
                }
                p.tick();
            }
            Iterator<Cable> it3 = this.om.cables.iterator();
            while (it3.hasNext()) {
                Cable p2 = it3.next();
                CableEnd e12 = (CableEnd) p2.g1;
                CableEnd e22 = (CableEnd) p2.g2;
                Iterator<GrabableObject> it4 = this.om.all.iterator();
                while (it4.hasNext()) {
                    GrabableObject o2 = it4.next();
                    if (e12.saved_oid != -1 && o2.__unique_id == e12.saved_oid) {
                        if (o2 instanceof Hub) {
                            e12.attach_to_hub((Hub) o2);
                        } else {
                            e12.attach_to(o2);
                        }
                    }
                    if (e22.saved_oid != -1 && o2.__unique_id == e22.saved_oid) {
                        if (o2 instanceof Hub) {
                            e22.attach_to_hub((Hub) o2);
                        } else {
                            e22.attach_to(o2);
                        }
                    }
                }
                p2.tick();
            }
        }
    }

    private void setup_corners(ObjectManager.Layer layer) {
        Iterator<MetalCorner> i = layer.metalcorners.iterator();
        while (i.hasNext()) {
            MetalCorner c = i.next();
            MetalBar o1 = null;
            MetalBar o2 = null;
            int n1 = c.b1_id;
            int n2 = c.b2_id;
            Iterator<MetalBar> it = layer.bars.iterator();
            while (it.hasNext()) {
                MetalBar o = it.next();
                if (o.__unique_id == n1) {
                    o1 = o;
                } else if (o.__unique_id == n2) {
                    o2 = o;
                }
                if (o1 != null && o2 != null) {
                    break;
                }
            }
            if (o1 == null || o2 == null || !c.setup(world, o1, o2)) {
                Gdx.app.log("ERROR", "invalid metal corner object ids");
                try {
                    c.dispose(world);
                } catch (Exception e) {
                }
                i.remove();
            }
        }
    }

    private void reload() {
        try {
            this.level.reload_objects();
            load();
        } catch (IOException e) {
            Gdx.app.log("FATAL", "Could not reload objects");
        }
    }

    public void update_ropes() {
        if (rope_quality > 100) {
            rope_quality = 100;
        }
        if (rope_quality < 0) {
            rope_quality = 0;
        }
        if (this.om.ropes.size() > 0) {
            Iterator<Rope> it = this.om.ropes.iterator();
            while (it.hasNext()) {
                BaseRope b = it.next();
                b.create_rope();
            }
        }
        if (this.om.cables.size() > 0) {
            Iterator<Cable> it2 = this.om.cables.iterator();
            while (it2.hasNext()) {
                BaseRope b2 = it2.next();
                b2.create_rope();
            }
        }
        if (this.om.pcables.size() > 0) {
            Iterator<PanelCable> it3 = this.om.pcables.iterator();
            while (it3.hasNext()) {
                BaseRope b3 = it3.next();
                b3.create_rope();
            }
        }
        Iterator<Rope> it4 = this.om.ropes.iterator();
        while (it4.hasNext()) {
            BaseRope r = it4.next();
            r.stabilize();
        }
        Iterator<Cable> it5 = this.om.cables.iterator();
        while (it5.hasNext()) {
            BaseRope r2 = it5.next();
            r2.stabilize();
        }
        Iterator<PanelCable> it6 = this.om.pcables.iterator();
        while (it6.hasNext()) {
            BaseRope r3 = it6.next();
            r3.stabilize();
        }
    }

    @Override // com.bithack.apparatus.Screen
    public void resume() {
        Gdx.input.setInputProcessor(this);
        G.set_clear_color(0.1f, 0.1f, 0.1f);
        System.gc();
        this.lowfpscount = 0;
        this.finished_fade = 0.0f;
        this.disable_undo = false;
        this.commit_next = false;
        this.grabbed_object = null;
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_dspeed);
        this.widgets.disable(this.widget_dforce);
        if (force_disable_shadows) {
            enable_shadows = false;
        } else {
            String tmp = Settings.get("enableshadows");
            enable_shadows = tmp.equals("") || tmp.equals("yes");
        }
        draw_fps = Settings.get("drawfps").equals("yes");
        draw_fps = Settings.get("drawfps").equals("yes");
        String tmp2 = Settings.get("ropequality");
        rope_quality = (tmp2 == null || tmp2.equals("")) ? 100 : Integer.parseInt(tmp2);
        String tmp3 = Settings.get("bloom");
        enable_bloom = tmp3.equals("") || tmp3.equals("yes");
        String tmp4 = Settings.get("reflection");
        enable_reflections = tmp4.equals("") || tmp4.equals("yes");
        String tmp5 = Settings.get("hqmeshes");
        enable_hqmeshes = tmp5.equals("") || tmp5.equals("yes");
        String tmp6 = Settings.get("enablebg");
        enable_bg = tmp6.equals("") || tmp6.equals("yes");
        this.camera_h.camera_pos.z = 17.0f;
        update_ropes();
        has_multitouch = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
        G.batch.setColor(Color.WHITE);
        if (level_type != 2) {
            set_mode(4);
        } else if (!sandbox) {
            pause_world();
            resume_world();
            set_mode(3);
        }
        MiscRenderer.update_quality();
    }

    @Override // com.bithack.apparatus.Screen
    public boolean screen_to_world(int x, int y, Vector2 out) {
        return false;
    }

    @Override // com.bithack.apparatus.Screen
    public boolean ready() {
        return this.ready;
    }

    public void autosave() {
        prepare_save();
        try {
            this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/.autosave.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public void autosave_challenge() {
        if (this.level_n != -1) {
            Gdx.app.log("autosave", "Autosaving challenge " + this.level_n + "/" + this.level_id);
            prepare_save();
            try {
                this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/.autosave_" + this.level_n + (this.level_category == 2 ? "_2" : "") + ".jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    public void save() {
        if (this.level_filename != null) {
            prepare_save();
            try {
                this.level.save_jar(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/" + this.level_filename.replace("/", "_") + ".jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
            this.modified = false;
        }
    }

    public void prepare_save() {
        BaseObject[] objects = (BaseObject[]) this.om.all.toArray(new BaseObject[this.om.all.size() + this.hinges.size()]);
        for (int x = this.om.all.size(); x < this.om.all.size() + this.hinges.size(); x++) {
            objects[x] = this.hinges.get(x - this.om.all.size());
        }
        this.level.set_objects(objects);
    }

    private void toggle_tracing() {
        if (tracing) {
            Settings.stop_tracing();
        } else {
            Settings.start_tracing();
        }
        tracing = !tracing;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int key) {
        Widget w;
        Widget w2;
        switch (key) {
            case 4:
            case Input.Keys.B /* 30 */:
                if (from_sandbox) {
                    end_challenge_testing();
                } else if (sandbox) {
                    if (this.modified) {
                        ApparatusApp.backend.open_ingame_sandbox_back_menu();
                    } else {
                        this.tp.open_mainmenu();
                    }
                } else if (from_community) {
                    ApparatusApp.backend.open_ingame_back_community_menu();
                } else {
                    if (this.state == 2) {
                        this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
                        pause_world();
                        save();
                        this.level_filename = null;
                    } else if (this.modified) {
                        if (mode == 3) {
                            pause_world();
                        }
                        autosave_challenge();
                        this.level_filename = null;
                    }
                    ApparatusApp.backend.open_ingame_back_menu();
                }
                Settings.save();
                return false;
            case 5:
            case 6:
            case 17:
            case 18:
            case 23:
            case 24:
            case Input.Keys.VOLUME_DOWN /* 25 */:
            case Input.Keys.POWER /* 26 */:
            case Input.Keys.CAMERA /* 27 */:
            case Input.Keys.CLEAR /* 28 */:
            case Input.Keys.C /* 31 */:
            case Input.Keys.H /* 36 */:
            case Input.Keys.I /* 37 */:
            case Input.Keys.J /* 38 */:
            case Input.Keys.K /* 39 */:
            case Input.Keys.L /* 40 */:
            case Input.Keys.V /* 50 */:
            case Input.Keys.Y /* 53 */:
            case Input.Keys.COMMA /* 55 */:
            case Input.Keys.PERIOD /* 56 */:
            case Input.Keys.ALT_LEFT /* 57 */:
            case Input.Keys.ALT_RIGHT /* 58 */:
            case Input.Keys.SHIFT_LEFT /* 59 */:
            case Input.Keys.SHIFT_RIGHT /* 60 */:
            case Input.Keys.TAB /* 61 */:
            case Input.Keys.SPACE /* 62 */:
            case Input.Keys.SYM /* 63 */:
            case 64:
            case Input.Keys.ENVELOPE /* 65 */:
            case Input.Keys.ENTER /* 66 */:
            case 67:
            case Input.Keys.GRAVE /* 68 */:
            case Input.Keys.MINUS /* 69 */:
            case Input.Keys.EQUALS /* 70 */:
            case Input.Keys.LEFT_BRACKET /* 71 */:
            case Input.Keys.RIGHT_BRACKET /* 72 */:
            case Input.Keys.BACKSLASH /* 73 */:
            case Input.Keys.SEMICOLON /* 74 */:
            case Input.Keys.APOSTROPHE /* 75 */:
            case Input.Keys.SLASH /* 76 */:
            case Input.Keys.AT /* 77 */:
            case Input.Keys.NUM /* 78 */:
            case Input.Keys.HEADSETHOOK /* 79 */:
            case Input.Keys.FOCUS /* 80 */:
            default:
                return false;
            case 7:
                set_bg(10);
                this.level_category = 2;
                return false;
            case 8:
                set_bg(1);
                return false;
            case 9:
                set_bg(2);
                return false;
            case 10:
                set_bg(3);
                return false;
            case 11:
                set_bg(4);
                return false;
            case 12:
                set_bg(5);
                return false;
            case 13:
                set_bg(6);
                return false;
            case 14:
                set_bg(7);
                return false;
            case 15:
                set_bg(8);
                return false;
            case 16:
                set_bg(9);
                return false;
            case 19:
            case Input.Keys.W /* 51 */:
                this.camera_h.add_velocity(0.0f, 1.0f);
                return false;
            case 20:
            case Input.Keys.S /* 47 */:
                this.camera_h.add_velocity(0.0f, -1.0f);
                return false;
            case 21:
                this.camera_h.add_velocity(-1.0f, 0.0f);
                return false;
            case 22:
                this.camera_h.add_velocity(1.0f, 0.0f);
                return false;
            case Input.Keys.A /* 29 */:
                if (!a_down && this.active_panel != null && (w2 = this.active_panel.find(0)) != null) {
                    w2.touch_down_local(0, 0);
                }
                a_down = true;
                return false;
            case 32:
                if (!d_down && this.active_panel != null && (w = this.active_panel.find(2)) != null) {
                    w.touch_down_local(0, 0);
                }
                d_down = true;
                return false;
            case Input.Keys.E /* 33 */:
                enable_bg = enable_bg ? false : true;
                return false;
            case Input.Keys.F /* 34 */:
                physics_stability = 2;
                return false;
            case Input.Keys.G /* 35 */:
                physics_stability = 1;
                return false;
            case Input.Keys.M /* 41 */:
                this.camera_h.camera_pos.z += 1.0f;
                return false;
            case Input.Keys.N /* 42 */:
                this.camera_h.camera_pos.z -= 1.0f;
                return false;
            case Input.Keys.O /* 43 */:
                open(new File(String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/TESTLEVEL.jar"));
                return false;
            case Input.Keys.P /* 44 */:
                this.um.undo();
                return false;
            case Input.Keys.Q /* 45 */:
                enable_menu = enable_menu ? false : true;
                return false;
            case Input.Keys.R /* 46 */:
                enable_shadows = enable_shadows ? false : true;
                return false;
            case Input.Keys.T /* 48 */:
                toggle_tracing();
                return false;
            case Input.Keys.U /* 49 */:
                this.level_filename = "TESTLEVEL";
                save();
                return false;
            case Input.Keys.X /* 52 */:
                BaseObject o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 18);
                Damper _d = (Damper) o;
                _d.g1.__unique_id = _d.__unique_id * 10000;
                _d.g2.__unique_id = _d.__unique_id * 10000000;
                this.om.add((GrabableObject) o);
                return false;
            case Input.Keys.Z /* 54 */:
                ChristmasGift oo = (ChristmasGift) ObjectFactory.adapter.create(world, 2, 17);
                oo.set_property("sx", Float.valueOf(0.25f + (0.125f * new Random().nextInt(4))));
                oo.set_property("sy", Float.valueOf(0.25f + (0.125f * new Random().nextInt(4))));
                Gdx.app.log("adding", "git");
                this.om.add(oo);
                return false;
            case Input.Keys.PLUS /* 81 */:
                set_bg(11);
                return false;
            case Input.Keys.MENU /* 82 */:
                if (sandbox) {
                    ApparatusApp.backend.open_ingame_sandbox_menu();
                } else {
                    ApparatusApp.backend.open_ingame_menu();
                }
                return false;
        }
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char arg0) {
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int key) {
        Widget w;
        Widget w2;
        switch (key) {
            case Input.Keys.A /* 29 */:
                a_down = false;
                if (this.active_panel != null && (w2 = this.active_panel.find(0)) != null) {
                    w2.touch_up_local(0, 0);
                }
                return false;
            case Input.Keys.B /* 30 */:
            case Input.Keys.C /* 31 */:
            default:
                return false;
            case 32:
                d_down = false;
                if (this.active_panel != null && (w = this.active_panel.find(2)) != null) {
                    w.touch_up_local(0, 0);
                }
                return false;
        }
    }

    void set_mode(int mode2) {
        switch (mode2) {
            case 0:
                mode = 0;
                break;
            case 1:
                mode = 1;
                break;
            case 3:
                mode = 3;
                break;
            case 4:
                mode = 4;
                break;
        }
    }

    private void destroy_drag_body() {
        if (this.drag_body != null) {
            world.destroyBody(this.drag_body);
            this.drag_body = null;
        }
        if (this.ground_joint != null) {
            world.destroyJoint(this.ground_joint);
            this.ground_joint = null;
        }
    }

    private void create_drag_body(float x, float y) {
        if (this.drag_body != null) {
            destroy_drag_body();
        }
        if (this.grabbed_object != null && this.grabbed_object.num_hinges == 0) {
            this._rjd.initialize(this.ground, this.grabbed_object.body, this.grabbed_object.get_position());
            this.ground_joint = world.createJoint(this._rjd);
        }
        this.drag_body = world.createBody(this.drag_bd);
        this.drag_body.createFixture(this.drag_fd);
        this.drag_body_target.set(x, y);
        this.drag_body.setType(BodyDef.BodyType.StaticBody);
        this.drag_body.setTransform(this.drag_body_target, 0.0f);
        this.drag_body.setType(BodyDef.BodyType.DynamicBody);
        this.drag_body.setUserData(this);
    }

    void pause_world() {
        this.state = 0;
        this.lowfpscount = 0;
        if (enable_multithreading && this.sim_thread != null) {
            this.sim_thread.terminate();
            this.sim_thread = null;
        }
        world.setGravity(new Vector2(0.0f, 0.0f));
        this.active_panel = null;
        this.camera_h.release_target();
        if (camera_reset && level_type != 2) {
            this.camera_h.load();
        }
        this.camera_h.velocity.set(0.0f, 0.0f, 0.0f);
        world.setContactFilter(this.contact_handler);
        world.setContactListener(this.contact_handler);
        Iterator<GrabableObject> it = this.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject o = it.next();
            o.set_active(false);
        }
        Iterator<GrabableObject> it2 = this.om.all.iterator();
        while (it2.hasNext()) {
            GrabableObject o2 = it2.next();
            o2.pause();
        }
        Iterator<GrabableObject> it3 = this.om.all.iterator();
        while (it3.hasNext()) {
            GrabableObject o3 = it3.next();
            o3.set_active(false);
        }
        Iterator<StaticMotor> it4 = this.om.static_motors.iterator();
        while (it4.hasNext()) {
            BaseMotor m = it4.next();
            m.joint_pause();
        }
        Iterator<DynamicMotor> it5 = this.om.layer0.dynamicmotors.iterator();
        while (it5.hasNext()) {
            BaseMotor m2 = it5.next();
            m2.joint_pause();
        }
        Iterator<DynamicMotor> it6 = this.om.layer1.dynamicmotors.iterator();
        while (it6.hasNext()) {
            BaseMotor m3 = it6.next();
            m3.joint_pause();
        }
        Iterator<GrabableObject> it7 = this.om.all.iterator();
        while (it7.hasNext()) {
            GrabableObject o4 = it7.next();
            o4.set_active(false);
        }
        Iterator<Hinge> it8 = this.hinges.iterator();
        while (it8.hasNext()) {
            Hinge h = it8.next();
            h.recreate(world);
        }
        Iterator<GrabableObject> it9 = this.om.all.iterator();
        while (it9.hasNext()) {
            GrabableObject o5 = it9.next();
            o5.pause();
        }
        Iterator<GrabableObject> it10 = this.om.all.iterator();
        while (it10.hasNext()) {
            GrabableObject o6 = it10.next();
            o6.set_active(true);
        }
        if (!sandbox) {
            create_metal_cache();
        }
        Iterator<Rope> it11 = this.om.ropes.iterator();
        while (it11.hasNext()) {
            it11.next().stabilize();
        }
        Iterator<PanelCable> it12 = this.om.pcables.iterator();
        while (it12.hasNext()) {
            it12.next().stabilize();
        }
        Iterator<Cable> it13 = this.om.cables.iterator();
        while (it13.hasNext()) {
            it13.next().stabilize();
        }
        this.contact_handler.clean();
        set_mode(4);
        do_connectanims = false;
        if (Level.version >= 3) {
            resolve_cable_connections();
        } else {
            Iterator<Cable> it14 = this.om.cables.iterator();
            while (it14.hasNext()) {
                Cable r = it14.next();
                r.g1.body.setActive(true);
                r.g1.body.setAwake(true);
                r.g2.body.setActive(true);
                r.g2.body.setAwake(true);
                r.g1.body.setTransform(this._tmp.set(r.g1.get_position().x + 1.0E-11f, r.g1.get_position().y), 0.0f);
                r.g2.body.setTransform(this._tmp.set(r.g2.get_position().x + 1.0E-11f, r.g2.get_position().y), 0.0f);
                r.g1.translate(r.g1.get_position().x, r.g1.get_position().y);
                r.g2.translate(r.g2.get_position().x, r.g2.get_position().y);
                r.g1.body.setFixedRotation(true);
                r.g2.body.setFixedRotation(false);
            }
            Iterator<PanelCable> it15 = this.om.pcables.iterator();
            while (it15.hasNext()) {
                PanelCable r2 = it15.next();
                r2.g1.body.setActive(true);
                r2.g1.body.setAwake(true);
                r2.g2.body.setActive(true);
                r2.g2.body.setAwake(true);
                r2.g1.body.setTransform(this._tmp.set(r2.g1.get_position().x + 1.0E-11f, r2.g1.get_position().y), 0.0f);
                r2.g2.body.setTransform(this._tmp.set(r2.g2.get_position().x + 1.0E-11f, r2.g2.get_position().y), 0.0f);
                r2.g1.translate(r2.g1.get_position().x, r2.g1.get_position().y);
                r2.g2.translate(r2.g2.get_position().x, r2.g2.get_position().y);
                r2.g1.body.setFixedRotation(true);
                r2.g2.body.setFixedRotation(false);
            }
            Iterator<Panel> it16 = this.om.layer0.controllers.iterator();
            while (it16.hasNext()) {
                Panel p = it16.next();
                p.body.setFixedRotation(false);
                p.translate(p.get_position().x, p.get_position().y);
            }
        }
        do_connectanims = true;
        this.num_balls_in_goal = 0;
        this.num_balls = 0;
        SoundManager.stop_all();
        this.time_last = System.nanoTime();
        this.time_accum = 0L;
        if (!sandbox && level_type == 2) {
            resume_world();
        }
        if (sandbox && this.modified) {
            autosave();
            this.last_autosave = System.currentTimeMillis();
        }
        world.step(1.0f, 20, 20);
    }

    public void resume_world() {
        this.state = 0;
        this.pending_follow = false;
        set_mode(3);
        this.lowfpscount = 0;
        this.contact_handler.reset();
        this.camera_h.velocity.set(0.0f, 0.0f, 0.0f);
        this.num_balls_in_goal = 0;
        this.num_balls = this.om.layer0.marbles.size() + this.om.layer1.marbles.size() + this.om.christmasgifts.size();
        world.setContactFilter(this.ingame_contact_handler);
        world.setContactListener(this.ingame_contact_handler);
        world.setGravity(new Vector2(0.0f, -9.8f));
        this.camera_h.save();
        Iterator<Hinge> it = this.hinges.iterator();
        while (it.hasNext()) {
            Hinge o = it.next();
            o.save();
        }
        Iterator<GrabableObject> it2 = this.om.all.iterator();
        while (it2.hasNext()) {
            GrabableObject o2 = it2.next();
            o2.set_active(false);
        }
        world.step(1.0f, HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        Iterator<StaticMotor> it3 = this.om.static_motors.iterator();
        while (it3.hasNext()) {
            BaseMotor m = it3.next();
            m.joint_play();
        }
        Iterator<DynamicMotor> it4 = this.om.layer0.dynamicmotors.iterator();
        while (it4.hasNext()) {
            BaseMotor m2 = it4.next();
            m2.joint_play();
        }
        Iterator<DynamicMotor> it5 = this.om.layer1.dynamicmotors.iterator();
        while (it5.hasNext()) {
            BaseMotor m3 = it5.next();
            m3.joint_play();
        }
        Iterator<Hinge> it6 = this.hinges.iterator();
        while (it6.hasNext()) {
            Hinge o3 = it6.next();
            o3.recreate(world);
        }
        Iterator<GrabableObject> it7 = this.om.all.iterator();
        while (it7.hasNext()) {
            GrabableObject o4 = it7.next();
            o4.play();
        }
        Iterator<GrabableObject> it8 = this.om.all.iterator();
        while (it8.hasNext()) {
            GrabableObject o5 = it8.next();
            o5.set_active(true);
        }
        System.gc();
        this.time_last = System.nanoTime();
        this.time_accum = 0L;
        this.game_start = System.currentTimeMillis();
        if (enable_multithreading) {
            this.sim_thread = new SimulationThread(this, null);
            this.sim_thread.setPriority(5);
            this.sim_thread.start();
        }
        Iterator<GrabableObject> it9 = this.om.all.iterator();
        while (it9.hasNext()) {
            GrabableObject o6 = it9.next();
            o6.set_active(false);
        }
        world.step(0.001f, 1, 1);
        Iterator<StaticMotor> it10 = this.om.static_motors.iterator();
        while (it10.hasNext()) {
            BaseMotor m4 = it10.next();
            m4.joint_play();
        }
        Iterator<DynamicMotor> it11 = this.om.layer0.dynamicmotors.iterator();
        while (it11.hasNext()) {
            BaseMotor m5 = it11.next();
            m5.joint_play();
        }
        Iterator<DynamicMotor> it12 = this.om.layer1.dynamicmotors.iterator();
        while (it12.hasNext()) {
            BaseMotor m6 = it12.next();
            m6.joint_play();
        }
        Iterator<Hinge> it13 = this.hinges.iterator();
        while (it13.hasNext()) {
            Hinge o7 = it13.next();
            o7.recreate(world);
        }
        Iterator<GrabableObject> it14 = this.om.all.iterator();
        while (it14.hasNext()) {
            GrabableObject o8 = it14.next();
            o8.play();
        }
        Iterator<GrabableObject> it15 = this.om.all.iterator();
        while (it15.hasNext()) {
            GrabableObject o9 = it15.next();
            o9.set_active(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void grab_object(GrabableObject grabableObject) {
        if (this.grabbed_object != null) {
            release_object();
        }
        this.grabbed_object = grabableObject;
        set_mode(1);
        if (sandbox) {
            if (grabableObject instanceof Bar) {
                Bar b = (Bar) grabableObject;
                this.widgets.enable(this.widget_size);
                this.widget_size.value = b.size.x / 2.0f <= 3.99f ? b.size.x / 2.0f > 1.99f ? 0 : -1 : 1;
            } else if (grabableObject instanceof Wheel) {
                Wheel w = (Wheel) grabableObject;
                this.widgets.enable(this.widget_size);
                this.widget_size.value = w.size <= 1.99f ? w.size > 0.99f ? 0 : -1 : 1;
            } else if (grabableObject instanceof BaseRopeEnd) {
                BaseRopeEnd b2 = (BaseRopeEnd) grabableObject;
                this.widgets.enable(this.widget_size);
                this.widget_size.value = ((b2.get_baserope().size / 5.0f) - 1.0f) - 1.0f;
                this.widget_size.value = Math.min(1.0f, this.widget_size.value);
                if (grabableObject instanceof RopeEnd) {
                    this.widgets.enable(this.widget_elasticity);
                    this.widget_elasticity.value = b2.get_baserope().rubber ? 1 : -1;
                }
            } else if (grabableObject instanceof DamperEnd) {
                DamperEnd e = (DamperEnd) grabableObject;
                this.widgets.enable(this.widget_dspeed);
                this.widgets.enable(this.widget_dforce);
                this.widget_dspeed.value = ((e.damper.speed / 200.0f) * 2.0f) - 1.0f;
                this.widget_dforce.value = ((e.damper.force / 1000.0f) * 2.0f) - 1.0f;
            } else if (grabableObject instanceof Battery) {
                Battery h = (Battery) grabableObject;
                this.widgets.enable(this.widget_current);
                this.widgets.enable(this.widget_voltage);
                this.widgets.enable(this.widget_sizeb);
                this.widget_sizeb.value = h.size != 1 ? 1 : -1;
                this.widget_current.value = (h.current * 2.0f) - 1.0f;
                this.widget_voltage.value = (h.voltage * 2.0f) - 1.0f;
            } else if (grabableObject instanceof RocketEngine) {
                this.widgets.enable(this.widget_thrust);
                this.widget_thrust.value = (((RocketEngine) grabableObject).thrust * 2.0f) - 1.0f;
            }
        }
        grabableObject.grab();
    }

    public void release_object() {
        this.widgets.disable(this.widget_size);
        this.widgets.disable(this.widget_current);
        this.widgets.disable(this.widget_thrust);
        this.widgets.disable(this.widget_sizeb);
        this.widgets.disable(this.widget_voltage);
        this.widgets.disable(this.widget_elasticity);
        this.widgets.disable(this.widget_dforce);
        this.widgets.disable(this.widget_dspeed);
        if (this.grabbed_object != null) {
            this.grabbed_object.release();
            this.last_grabbed = this.grabbed_object;
            this.grabbed_object = null;
            set_mode(4);
        }
    }

    void remove_selected() {
        if (this.grabbed_object != null) {
            GrabableObject o = this.grabbed_object;
            release_object();
            if (!this.disable_undo) {
                this.um.begin_step(0, null);
                this.um.add_object(o);
            }
            remove_object(o);
            if (!this.disable_undo) {
                this.um.commit_step();
            }
            this.grabbed_object = null;
            set_mode(4);
            if (this.last_grabbed == o) {
                this.last_grabbed = null;
            }
        }
    }

    void remove_object(GrabableObject o) {
        if (o instanceof RopeEnd) {
            this.om.remove(o);
            remove_potential_fixture_pair(((RopeEnd) o).rope.g1.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((RopeEnd) o).rope.g2.body.getFixtureList().get(0));
            remove_potential_hinges(((RopeEnd) o).rope.g1);
            remove_potential_hinges(((RopeEnd) o).rope.g2);
            ((RopeEnd) o).rope.dispose(world);
            this.grabbed_object = null;
            set_mode(4);
            return;
        }
        if (o instanceof CableEnd) {
            this.om.remove(o);
            ((CableEnd) o).cable.dispose(world);
            this.grabbed_object = null;
            set_mode(4);
            return;
        }
        if (o instanceof PanelCableEnd) {
            this.om.remove(o);
            ((PanelCableEnd) o).cable.dispose(world);
            this.grabbed_object = null;
            set_mode(4);
            return;
        }
        if (o instanceof DamperEnd) {
            this.om.remove(((DamperEnd) o).damper);
            remove_potential_fixture_pair(((DamperEnd) o).damper.g1.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g1.body.getFixtureList().get(1));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g2.body.getFixtureList().get(0));
            remove_potential_fixture_pair(((DamperEnd) o).damper.g2.body.getFixtureList().get(1));
            remove_potential_hinges(((DamperEnd) o).damper.g1);
            remove_potential_hinges(((DamperEnd) o).damper.g2);
            ((DamperEnd) o).damper.dispose(world);
            this.grabbed_object = null;
            set_mode(4);
            return;
        }
        this.om.remove(o);
        remove_potential_hinges(o);
        if (o.body != null) {
            remove_potential_fixture_pair(o.body);
        }
        o.dispose(world);
    }

    void remove_potential_hinges(GrabableObject o) {
        remove_potential_hinges(o, false, true);
    }

    private void remove_potential_hinges(GrabableObject o, boolean anim, boolean override_fixed) {
        if (o instanceof BaseMotor) {
            if (!this.disable_undo) {
                this.um.save_basemotor((BaseMotor) o);
            }
            ((BaseMotor) o).detach();
            if (anim) {
                connectanims.add(new ConnectAnim(1, o.get_position().x, o.get_position().y));
            }
            if (o instanceof DynamicMotor) {
                Iterator<Hinge> i = this.hinges.iterator();
                while (i.hasNext()) {
                    Hinge h = i.next();
                    if (!h.fixed || override_fixed) {
                        if (h.body1_id == o.__unique_id || h.body2_id == o.__unique_id) {
                            if (!this.disable_undo) {
                                this.um.save_hinge(h);
                            }
                            if (anim) {
                                connectanims.add(new ConnectAnim(1, h.get_position().x, h.get_position().y));
                            }
                            h.dispose(world);
                            i.remove();
                        }
                    }
                }
                return;
            }
            return;
        }
        Iterator<Hinge> i2 = this.hinges.iterator();
        while (i2.hasNext()) {
            Hinge h2 = i2.next();
            if (!h2.fixed || override_fixed) {
                if (h2.body1_id == o.__unique_id || h2.body2_id == o.__unique_id) {
                    if (!this.disable_undo) {
                        this.um.save_hinge(h2);
                    }
                    if (anim) {
                        connectanims.add(new ConnectAnim(1, h2.get_position().x, h2.get_position().y));
                    }
                    h2.dispose(world);
                    i2.remove();
                }
            }
        }
        Iterator<StaticMotor> it = this.om.static_motors.iterator();
        while (it.hasNext()) {
            StaticMotor h3 = it.next();
            if (!h3.fixed || override_fixed) {
                if (h3.attached_object == o) {
                    if (!this.disable_undo) {
                        this.um.save_basemotor(h3);
                    }
                    if (anim) {
                        connectanims.add(new ConnectAnim(1, h3.get_position().x, h3.get_position().y));
                    }
                    h3.detach();
                }
            }
        }
        Iterator<DynamicMotor> it2 = this.om.layer0.dynamicmotors.iterator();
        while (it2.hasNext()) {
            DynamicMotor h4 = it2.next();
            if (!h4.fixed || override_fixed) {
                if (h4.attached_object == o) {
                    if (!this.disable_undo) {
                        this.um.save_basemotor(h4);
                    }
                    if (anim) {
                        connectanims.add(new ConnectAnim(1, h4.get_position().x, h4.get_position().y));
                    }
                    h4.detach();
                }
            }
        }
        Iterator<DynamicMotor> it3 = this.om.layer1.dynamicmotors.iterator();
        while (it3.hasNext()) {
            DynamicMotor h5 = it3.next();
            if (!h5.fixed || override_fixed) {
                if (h5.attached_object == o) {
                    if (!this.disable_undo) {
                        this.um.save_basemotor(h5);
                    }
                    if (anim) {
                        connectanims.add(new ConnectAnim(1, h5.get_position().x, h5.get_position().y));
                    }
                    h5.detach();
                }
            }
        }
    }

    public void remove_potential_fixture_pair(Body b) {
        Iterator<Fixture> it = b.getFixtureList().iterator();
        while (it.hasNext()) {
            Fixture a = it.next();
            remove_potential_fixture_pair(a);
        }
    }

    public void remove_potential_fixture_pair(Fixture a) {
        if (a != null) {
            for (int x = 0; x < ContactHandler.num_fixture_pairs; x++) {
                ContactHandler.FixturePair pair = ContactHandler.fixture_pairs[x];
                if (pair.a == a || pair.b == a) {
                    pair.invalid = true;
                }
            }
        }
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int p, int button) {
        if (p >= this.widget.length) {
            return true;
        }
        this.widget[p] = false;
        Gdx.app.log("x ", "x " + x);
        if (this.state == 2) {
            if (x > 0.4f * G.realwidth && x < 0.6195313f * G.realwidth && y > 0.5f * G.realheight && y < 0.62777776f * G.realheight) {
                if (this.level_n != -1) {
                    int next = this.level_n + 1;
                    SoundManager.play_startlevel();
                    ApparatusApp.instance.play(level_type, next);
                    this.do_save = true;
                } else {
                    Gdx.app.log("autosave", "opening community!");
                    ApparatusApp.backend.open_community();
                }
            }
            if (x > 0.4f * G.realwidth && x < 0.6039063f * G.realwidth && y > 0.69027776f * G.realheight && y < 0.82916665f * G.realheight) {
                if (this.level_n == -1) {
                    Settings.msg(L.get("uploading_solution_error"));
                    pause_world();
                    this.state = 0;
                } else {
                    if (this.level_category == 2) {
                    }
                    sandbox = true;
                    this.state = 0;
                    level_type = 0;
                    this.level_filename = ".lvl" + this.level_n + (this.level_category != 0 ? "_" + this.level_category : "");
                    this.level.type = "apparatus";
                    pause_world();
                    this.do_save = false;
                    save();
                    this.level_filename = "";
                    from_game = true;
                }
            }
        }
        if (p < 3) {
            this.num_touch_points++;
            Vector2 v = this._last_vec[p].set(x, G.realheight - y);
            this._touch_vec[p].set(this._last_vec[p]);
            if (p == 1) {
                this._last_dist = this._touch_vec[1].dst(this._touch_vec[0]);
                this.allow_swipe = false;
            } else if (p == 0) {
                this.prevent_nail = false;
                this._last_touch.set(x, y);
                this.undo_begun = false;
            } else {
                this.allow_swipe = false;
            }
            if (mode != 3) {
                this.widget[p] = true;
                this._tmpv.set(v.x * (G.width / G.realwidth), v.y * (G.height / G.realheight));
                Gdx.app.log("pospos", "pos " + this._tmpv.x + " " + this._tmpv.y);
                if ((!sandbox || !touch_handle_sandbox_btns(this._tmpv, p)) && ((this.grabbed_object == null || !touch_handle_object_btns(this._tmpv)) && !touch_handle_left_btns(this._tmpv, p))) {
                    this.widget[p] = false;
                    long time = System.currentTimeMillis();
                    if (!from_community || level_type == 1) {
                        if (p == 0) {
                            if (this.grabbed_object != null && !this.grabbed_object.fixed_rotation && !(this.grabbed_object instanceof Wheel)) {
                                Ray r = G.p_cam.getPickRay(x, y);
                                Intersector.intersectRayPlane(r, yaxis, this.iresult);
                                float angle = this.grabbed_object.get_state().angle;
                                this.tmp3.set(this.grabbed_object.get_state().position.x, this.grabbed_object.get_state().position.y, 0.0f);
                                float size = 2.0f;
                                if (this.grabbed_object instanceof Bar) {
                                    size = (((Bar) this.grabbed_object).size.x / 2.0f) + 2.0f;
                                }
                                this.tmp3.x = (float) (r10.x + (Math.cos(angle) * (0.5f + size)));
                                this.tmp3.y = (float) (r10.y + (Math.sin(angle) * (0.5f + size)));
                                if (this.iresult.dst(this.tmp3) < 1.25f + (this.camera_h.camera_pos.z / 80.0f)) {
                                    this.rotate_point.set(this.grabbed_object.get_state().position);
                                    if (this.grabbed_object instanceof MetalBar) {
                                        ((MetalBar) this.grabbed_object).escape_corners();
                                    }
                                    this.rotating = true;
                                    this.um.begin_step(2, this.grabbed_object);
                                    this.last_touch_time = time;
                                }
                            }
                            Body result = raycast_find_body(x, y);
                            if (result != null) {
                                this.grab_offs.set(this._tmp);
                                this.grab_offs.sub(result.getPosition());
                                destroy_drag_body();
                                grab_object((GrabableObject) result.getUserData());
                                set_mode(1);
                            } else {
                                set_mode(4);
                            }
                            this.last_touch_time = time;
                        } else {
                            this.last_touch_time = time;
                        }
                    }
                }
            } else {
                this._tmpv.set(v.x * (G.width / G.realwidth), v.y * (G.height / G.realheight));
                if (!touch_handle_left_btns(this._tmpv, p) && p == 0) {
                    if (this.mousejoint != null) {
                        world.destroyJoint(this.mousejoint);
                        this.mousejoint = null;
                    }
                    Body result2 = raycast_find_body(x, y);
                    if (result2 != null) {
                        if (this.pending_follow) {
                            this.pending_follow = false;
                            this.camera_h.set_target((GrabableObject) this.query_result_body.getUserData(), 3.0f, -3.0f);
                            this.state = 0;
                            this.time_last = System.nanoTime();
                            this.time_accum = 0L;
                            world.step(0.001f, 1, 1);
                        } else if (result2.getUserData() instanceof Panel) {
                            this.active_panel = (Panel) result2.getUserData();
                        } else if (result2.getUserData() instanceof Knob) {
                            _mjd.bodyA = this.ground;
                            _mjd.bodyB = result2;
                            _mjd.maxForce = ((Knob) result2.getUserData()).size * 3000.0f;
                            _mjd.frequencyHz = 10.0f;
                            _mjd.dampingRatio = 2.0f;
                            _mjd.target.set(result2.getWorldCenter());
                            this.mousejoint = (MouseJoint) world.createJoint(_mjd);
                            this.widget[p] = true;
                        }
                    } else if (this.pending_follow) {
                        this.camera_h.release_target();
                        this.pending_follow = false;
                        this.state = 0;
                        this.time_last = System.nanoTime();
                        this.time_accum = 0L;
                        world.step(0.001f, 1, 1);
                    }
                }
            }
        }
        return true;
    }

    private void open_hinge_menu(ContactHandler.FixturePair pair) {
        this.wrench_anim_pos.set(pair.point);
        this.hingeselect = true;
        this.hingepair = pair;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean touch_handle_object_btns(Vector2 vector2) {
        boolean z = false;
        int i = G.height;
        int i2 = G.width;
        this.um.begin_step(5, this.grabbed_object);
        if (sandbox) {
            if (!this.widgets.touch_down((int) vector2.x, ((int) vector2.y) - ((fix_bottombar ? 1 : 0) * 64))) {
                if (vector2.y > i - 64 && vector2.x > i2 - 192) {
                    switch ((((int) vector2.x) - (i2 - 192)) / 64) {
                        case 0:
                            if (this.grabbed_object instanceof BaseMotor) {
                                ((BaseMotor) this.grabbed_object).dir = ((BaseMotor) this.grabbed_object).dir > 0.0f ? -1 : 1;
                                z = true;
                                break;
                            }
                            break;
                        case 1:
                            if (this.grabbed_object.num_hinges == 0) {
                                Settings.msg(L.get("notattached"));
                                this.um.cancel_step();
                            } else {
                                this.um.begin_step(4, this.grabbed_object);
                                remove_potential_hinges(this.grabbed_object, true, sandbox);
                                this.um.commit_step();
                                SoundManager.play_detach();
                            }
                            z = true;
                            break;
                        case 2:
                            if (this.grabbed_object instanceof Battery) {
                                Battery battery = (Battery) this.grabbed_object;
                                Battery battery2 = (Battery) this.grabbed_object;
                                boolean z2 = ((Battery) this.grabbed_object).real_on ? false : true;
                                battery2.real_on = z2;
                                battery.on = z2;
                            } else if (this.grabbed_object instanceof StaticMotor) {
                                Settings.msg(L.get("motorcantmove"));
                                this.um.cancel_step();
                            } else if (this.grabbed_object instanceof Panel) {
                                ((Panel) this.grabbed_object).set_type((((Panel) this.grabbed_object).type + 1) % Panel.num_types);
                            } else if (this.grabbed_object.num_hinges == 0) {
                                if (!this.grabbed_object.fixed_layer) {
                                    if (this.level_n >= 7 || this.level_n == -1 || this.level_category == 2) {
                                        if (sandbox && (this.grabbed_object instanceof Plank)) {
                                            this.om.relayer(this.grabbed_object, (this.grabbed_object.layer + 1) % 3);
                                        } else if (this.grabbed_object instanceof DamperEnd) {
                                            Damper damper = ((DamperEnd) this.grabbed_object).damper;
                                            if (damper.g1.num_hinges == 0 && damper.g2.num_hinges == 0) {
                                                this.om.relayer(damper, this.grabbed_object.layer != 1 ? 1 : 0);
                                            } else {
                                                Settings.msg(L.get("mustdetach"));
                                            }
                                        } else {
                                            this.om.relayer(this.grabbed_object, this.grabbed_object.layer != 1 ? 1 : 0);
                                        }
                                        if (this.grabbed_object.body != null) {
                                            remove_potential_fixture_pair(this.grabbed_object.body);
                                        }
                                        this.grabbed_object.reshape();
                                    } else {
                                        Settings.msg(L.get("btnavailablelvl8"));
                                        this.um.cancel_step();
                                    }
                                }
                            } else if (this.level_n < 7 && this.level_n != -1 && this.level_category != 2) {
                                Settings.msg(L.get("btnavailablelvl8"));
                                this.um.cancel_step();
                            } else {
                                Settings.msg(L.get("mustdetach"));
                                this.um.cancel_step();
                            }
                            z = true;
                            break;
                    }
                } else if (vector2.x > i2 - 64) {
                    switch ((i - ((int) vector2.y)) / 64) {
                        case 1:
                            if (sandbox) {
                                remove_selected();
                                z = true;
                                break;
                            }
                            break;
                    }
                }
            } else {
                z = true;
            }
        }
        if (z) {
            this.modified = true;
        } else {
            this.um.cancel_step();
        }
        return z;
    }

    private boolean touch_handle_left_btns(Vector2 v, int p) {
        boolean r = false;
        int h = G.height;
        int i = G.width;
        if (this.hingeselect) {
            this.tmp3.set(this.wrench_anim_pos.x, this.wrench_anim_pos.y, 1.0f);
            G.p_cam.project(this.tmp3);
            this.tmp3.set(this.tmp3.x * (G.width / G.realwidth), this.tmp3.y * (G.height / G.realheight), 0.0f);
            if (v.x > this.tmp3.x && v.x < this.tmp3.x + 128.0f && v.y > this.tmp3.y && v.y < this.tmp3.y + 64.0f) {
                int btn = (((int) v.x) - ((int) this.tmp3.x)) / 64;
                this.wrench_anim_start = System.currentTimeMillis();
                if (btn == 1) {
                    SoundManager.play_hammer();
                }
                create_hinge(this.hingepair, btn);
                this.hingepair.invalid = true;
                this.hingeselect = false;
                return true;
            }
            this.hingeselect = false;
        }
        if (mode == 3) {
            if (this.active_panel != null) {
                this._tmpv.set(v.x, v.y);
                if (this.active_panel.widgets.touch_down((int) this._tmpv.x, (int) this._tmpv.y, p)) {
                    this.widget[p] = true;
                    return true;
                }
            }
            if (v.y > G.height - 64 && v.x > G.width - 64) {
                this.pending_follow = true;
                this.widget[p] = true;
                return true;
            }
        }
        if (p == 0 && v.x < 64.0f) {
            switch ((h - ((int) v.y)) / 56) {
                case 0:
                    if (mode != 3) {
                        if (sandbox && !testing_challenge && level_type == 1) {
                            ApparatusApp.backend.open_sandbox_play_options();
                        } else {
                            resume_world();
                        }
                    } else {
                        pause_world();
                        pause_world();
                    }
                    r = true;
                    break;
                case 1:
                    if (mode != 3) {
                        this.um.undo();
                        r = true;
                        break;
                    }
                    break;
                case 2:
                    if (mode != 3) {
                        if (this.msg != null) {
                            ApparatusApp.backend.open_infobox(this.msg);
                            r = true;
                            break;
                        }
                    } else if (level_type == 2 && this.msg != null) {
                        ApparatusApp.backend.open_infobox(this.msg);
                        r = true;
                        break;
                    }
                    break;
                case 3:
                    if (!has_multitouch) {
                        this.camera_h.camera_pos.z += 4.0f;
                    }
                    r = true;
                    break;
                case 4:
                    if (!has_multitouch) {
                        this.camera_h.camera_pos.z -= 4.0f;
                    }
                    r = true;
                    break;
            }
        } else if (p == 0 && sandbox && from_game && mode != 3 && v.x < 208.0f && v.y > h - 48) {
            release_object();
            SoundManager.play_startlevel();
            open(this.level_category, this.level_n + 1);
            this.num_touch_points = 0;
            level_type = 1;
        }
        return r;
    }

    private boolean touch_handle_sandbox_category_btns(int offs, int p) {
        if (offs >= this.sandbox_category_cache_id.length) {
            return false;
        }
        this.open_sandbox_category = offs;
        this.open_animate_dir = 1;
        this.open_animate_time = 0.0f;
        this.widget[p] = true;
        return true;
    }

    private boolean touch_handle_sandbox_btns(Vector2 v, int p) {
        boolean ret = false;
        int i = G.height;
        int w = G.width;
        if (v.y - ((fix_bottombar ? 1 : 0) * 64) < 64.0f) {
            ret = true;
            GrabableObject o = null;
            int offs = (w - ((int) v.x)) / 56;
            Gdx.app.log("offs", new StringBuilder().append(offs).toString());
            if (this.open_sandbox_category == -1) {
                return touch_handle_sandbox_category_btns(offs, p);
            }
            if (offs == 0) {
                this.open_animate_dir = -1;
                this.open_animate_time = 0.2f;
                return true;
            }
            switch (this.open_sandbox_category) {
                case 0:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 13);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 15);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 14);
                            break;
                        case 3:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 16);
                            break;
                        case 4:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 18);
                            Damper _d = (Damper) o;
                            _d.g1.__unique_id = _d.__unique_id * 10000;
                            _d.g2.__unique_id = _d.__unique_id * 10000000;
                            break;
                    }
                case 1:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 1);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 2);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 12);
                            break;
                    }
                case 2:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 3, 0);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 8);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 7);
                            break;
                        case 3:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 6);
                            Cable _pc = (Cable) o;
                            _pc.g1.__unique_id = _pc.__unique_id * 10000;
                            _pc.g2.__unique_id = _pc.__unique_id * 10000000;
                            break;
                        case 4:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 11);
                            PanelCable _p = (PanelCable) o;
                            _p.g1.__unique_id = _p.__unique_id * 10000;
                            _p.g2.__unique_id = _p.__unique_id * 10000000;
                            break;
                        case 5:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 10);
                            break;
                    }
                case 3:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 3);
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 0);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 1, 1);
                            break;
                    }
                case 4:
                    switch (offs - 1) {
                        case 0:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 5);
                            Rope r = (Rope) o;
                            r.g1.__unique_id = r.__unique_id * 10000;
                            r.g2.__unique_id = r.__unique_id * 10000000;
                            break;
                        case 1:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 0);
                            break;
                        case 2:
                            o = (GrabableObject) ObjectFactory.adapter.create(world, 2, 4);
                            break;
                    }
            }
            if (o != null) {
                this.modified = true;
                this.ghost_object = o;
                o.ghost = true;
                this.pending_ghost = true;
                this.dragging_ghost = true;
                if (o instanceof BaseRope) {
                    ((BaseRope) o).g1.ghost = true;
                    ((BaseRope) o).g2.ghost = true;
                    ((BaseRope) o).g1.translate(-1.0f, -20000.0f);
                    ((BaseRope) o).g2.translate(1.0f, -20000.0f);
                } else if (o instanceof PanelCable) {
                    ((PanelCable) o).g1.ghost = true;
                    ((PanelCable) o).g2.ghost = true;
                    ((PanelCable) o).g1.translate(-1.0f, -20000.0f);
                    ((PanelCable) o).g2.translate(1.0f, -20000.0f);
                } else if (o instanceof Damper) {
                    ((Damper) o).g1.ghost = true;
                    ((Damper) o).g2.ghost = true;
                    ((Damper) o).g1.translate(-20000.0f, ((Damper) o).size / 2.0f);
                    ((Damper) o).g2.translate(-20000.0f, (-((Damper) o).size) / 2.0f);
                } else {
                    o.translate(0.0f, -20000.0f);
                }
                o.save_state();
                o.pause();
                if ((o instanceof MetalBar) || (o instanceof Marble) || (o instanceof StaticMotor)) {
                    o.body.setType(BodyDef.BodyType.DynamicBody);
                }
                this.widget[p] = false;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int p, int button) {
        if (p < this.widget.length && p < 3) {
            this.num_touch_points--;
            if (this.num_touch_points <= 0) {
                this.num_touch_points = 0;
                this.allow_swipe = true;
            }
            if (this.widget[p] && mode != 3) {
                this.um.commit_step();
            }
            if (p == 0 && mode != 3) {
                long time = System.currentTimeMillis();
                Ray r = G.p_cam.getPickRay(x, y);
                Intersector.intersectRayPlane(r, yaxis, this.tmp3);
                if (time - this.last_touch_time < 333 && time - this.wrench_anim_start > 150) {
                    ContactHandler.FixturePair found_pair = null;
                    float found_dst = 0.0f;
                    for (int f = 0; f < ContactHandler.num_fixture_pairs; f++) {
                        ContactHandler.FixturePair fp = ContactHandler.fixture_pairs[f];
                        Vector2 pt = fp.get_intersection_point();
                        if (pt != null && (fp.a.getBody().getUserData() == this.last_grabbed || fp.b.getBody().getUserData() == this.last_grabbed)) {
                            float dist = pt.dst(this.tmp3.x, this.tmp3.y);
                            if (dist < 3.0f && (found_pair == null || dist < found_dst)) {
                                found_dst = pt.dst(this.tmp3.x, this.tmp3.y);
                                found_pair = fp;
                            }
                        }
                    }
                    if (found_pair != null) {
                        if (found_pair.same_layer || (found_pair.a.getBody().getUserData() instanceof BaseMotor) || (found_pair.b.getBody().getUserData() instanceof BaseMotor)) {
                            create_hinge(found_pair, 1);
                            SoundManager.play_hammer();
                            found_pair.invalid = true;
                        } else if (this.level_n > -1 && this.level_n < 12 && this.level_category != 2) {
                            SoundManager.play_hammer();
                            create_hinge(found_pair, 0);
                            found_pair.invalid = true;
                        } else {
                            open_hinge_menu(found_pair);
                        }
                    }
                }
                if (this.dragging_ghost) {
                    release_ghost();
                }
                if (this.rotating && time - this.last_touch_time < 100) {
                    Body result = raycast_find_body(x, y);
                    if (result != null) {
                        this.grab_offs.set(this._tmp);
                        this.grab_offs.sub(result.getPosition());
                        destroy_drag_body();
                        grab_object((GrabableObject) result.getUserData());
                        set_mode(1);
                    } else {
                        set_mode(4);
                    }
                }
                this.rotating = false;
                if (this.grabbed_object != null) {
                    this.grabbed_object.release();
                    if (System.currentTimeMillis() - this.last_touch_time < 200 && this.drag_body != null) {
                        release_object();
                    }
                }
                destroy_drag_body();
                this.commit_next = true;
            } else if (mode == 3) {
                if (p == 0 && this.mousejoint != null) {
                    world.destroyJoint(this.mousejoint);
                    this.mousejoint = null;
                }
                if (this.widget[p] && this.active_panel != null) {
                    this.active_panel.widgets.touch_up(x, y, p);
                }
            }
        }
        return true;
    }

    private void release_ghost() {
        this.dragging_ghost = false;
        this.ghost_object.ghost = false;
        world.step(0.001f, 20, 20);
        release_object();
        world.step(0.001f, 20, 20);
        if (this.ghost_object instanceof BaseRope) {
            ((BaseRope) this.ghost_object).g1.ghost = false;
            ((BaseRope) this.ghost_object).g2.ghost = false;
        } else if (this.ghost_object instanceof Damper) {
            ((Damper) this.ghost_object).g1.ghost = false;
            ((Damper) this.ghost_object).g2.ghost = false;
        }
        if (this.ghost_object.get_position().y < -10000.0f || this.ghost_object.get_position().x < -10000.0f) {
            Settings.msg(L.get("dragbtn"));
            this.ghost_object.dispose(world);
        } else {
            this.om.add(this.ghost_object);
            this.ghost_object.reshape();
            this.ghost_object.pause();
            if (this.ghost_object instanceof Damper) {
                grab_object(((Damper) this.ghost_object).g1);
            } else if (!(this.ghost_object instanceof BaseRope)) {
                grab_object(this.ghost_object);
            } else {
                grab_object(((BaseRope) this.ghost_object).g1);
            }
            this.um.begin_step(1, this.ghost_object);
        }
        this.ghost_object = null;
    }

    private void create_hinge(ContactHandler.FixturePair fp, int type) {
        GrabableObject a = (GrabableObject) fp.a.getBody().getUserData();
        GrabableObject b = (GrabableObject) fp.b.getBody().getUserData();
        if (fp.same_layer) {
            type = 1;
        } else {
            if (a instanceof BaseMotor) {
                attach_to_motor((BaseMotor) a, b);
                ((BaseMotor) a).fixed = sandbox;
                connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
                return;
            }
            if (b instanceof BaseMotor) {
                attach_to_motor((BaseMotor) b, a);
                ((BaseMotor) b).fixed = sandbox;
                connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
                return;
            }
        }
        world.step(0.001f, HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        this.um.begin_step(3, null);
        Hinge hinge = (Hinge) ObjectFactory.adapter.create(world, 3, 1);
        hinge.type = type;
        hinge.same_layer = fp.same_layer;
        if ((a instanceof DynamicMotor) || (a instanceof Panel) || (a instanceof Hub)) {
            hinge.rot_extra = ((Vector2) fp.a.getUserData()).x * 90.0f;
        } else if ((a instanceof RocketEngine) || (a instanceof DamperEnd)) {
            hinge.rot_extra = 180.0f;
        }
        hinge.setup((GrabableObject) fp.a.getBody().getUserData(), (GrabableObject) fp.b.getBody().getUserData(), fp.point);
        if (sandbox) {
            hinge.fixed = true;
        } else {
            hinge.fixed = false;
        }
        this.hinges.add(hinge);
        this.um.add_hinge(hinge);
        this.um.commit_step();
        connectanims.add(new ConnectAnim(0, fp.point.x, fp.point.y));
    }

    private void attach_to_motor(BaseMotor h, GrabableObject b) {
        this.um.begin_step(3, null);
        if (b instanceof Plank) {
            Plank plank = (Plank) b;
            float aa = plank.body.getAngle();
            this._tmp.set((float) Math.cos(aa), (float) Math.sin(aa));
            this._tmp.mul(50.0f);
            this._tmp2.set(this._tmp);
            this._tmp3.set(this._tmp2).mul(-1.0f);
            this._tmp2.add(plank.get_position());
            this._tmp3.add(plank.get_position());
            float dist = Intersector.intersectSegmentCircleDisplace(this._tmp2, this._tmp3, h.get_position(), 1.0f, this._tmp);
            if (dist != Float.POSITIVE_INFINITY) {
                this._tmp.mul(-dist);
                b.translate(b.get_position().x - this._tmp.x, b.get_position().y - this._tmp.y);
            } else {
                Gdx.app.log("!!!ERROR!!!", "THIS ERROR SHOULD NEVER OCCUR !!!!!!!!!!!!!!!!!!!!!!!!");
            }
            h.attach(b);
            this.um.add_motor_hinge(h, b);
        } else if (b instanceof Wheel) {
            Wheel wheel = (Wheel) b;
            wheel.translate(h.get_position().x, h.get_position().y);
            h.attach(b);
            this.um.add_motor_hinge(h, b);
        }
        world.step(1.0E-4f, 1, 1);
        this.um.commit_step();
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int p) {
        if (p >= this.widget.length) {
            return true;
        }
        this.fpos.set(x, y);
        if (p < 3) {
            this._last_vec[p].set(this._touch_vec[p]);
            this._touch_vec[p].set(x, G.realheight - y);
            if (this.widget[p]) {
                this._tmpv.set(x * (G.width / G.realwidth), y * (G.height / G.realheight));
                if (mode == 3) {
                    if (this.active_panel != null) {
                        this.active_panel.widgets.touch_drag((int) this._tmpv.x, (int) this._tmpv.y, p);
                    }
                } else if (sandbox && !this.rotating) {
                    this.widgets.touch_drag((int) this._tmpv.x, ((int) this._tmpv.y) - ((fix_bottombar ? 1 : 0) * 64));
                }
                if (this.mousejoint != null && p == 0) {
                    Ray r = G.p_cam.getPickRay(x, y);
                    Intersector.intersectRayPlane(r, yaxis, this.iresult);
                    this.mousejoint.setTarget(this._tmp.set(this.iresult.x, this.iresult.y));
                }
                this.modified = true;
            } else if (p == 0) {
                Ray r2 = G.p_cam.getPickRay(x, y);
                Intersector.intersectRayPlane(r2, yaxis, this.iresult);
                long now = System.currentTimeMillis();
                if (now - this.last_touch_time < 80) {
                    this.prevent_nail = false;
                    if (this.grabbed_object != null) {
                        this.grab_offs.set(this.iresult.x, this.iresult.y);
                        this.grab_offs.set(this._tmp);
                        this.grab_offs.sub(this.grabbed_object.get_position());
                    }
                } else {
                    this.prevent_nail = true;
                    if (this.dragging_ghost) {
                        if (this.pending_ghost && y < G.realheight - 64) {
                            this.pending_ghost = false;
                        }
                        if (!this.pending_ghost) {
                            this._tmp.set(this.iresult.x, this.iresult.y);
                            if (this.ghost_object instanceof BaseRope) {
                                ((BaseRope) this.ghost_object).g1.translate(this._tmp.x - 1.0f, this._tmp.y);
                                ((BaseRope) this.ghost_object).g2.translate(this._tmp.x + 2.0f, this._tmp.y);
                                ((BaseRope) this.ghost_object).save_state();
                            } else if (this.ghost_object instanceof Damper) {
                                ((Damper) this.ghost_object).g1.translate(this._tmp.x, (((Damper) this.ghost_object).size / 2.0f) + this._tmp.y);
                                ((Damper) this.ghost_object).g2.translate(this._tmp.x, this._tmp.y - (((Damper) this.ghost_object).size / 2.0f));
                                ((Damper) this.ghost_object).save_state();
                            } else {
                                this.ghost_object.translate(this._tmp.x, this._tmp.y);
                                this.ghost_object.save_state();
                            }
                            this.modified = true;
                        }
                    } else {
                        switch (mode) {
                            case 1:
                                if (!this.undo_begun && !this.dragging_ghost) {
                                    this.um.begin_step(2, this.grabbed_object);
                                    this.undo_begun = true;
                                }
                                if (this.grabbed_object != null && !this.rotating) {
                                    this._tmp.set(this.iresult.x, this.iresult.y);
                                    this._tmp.sub(this.grab_offs);
                                    if (this.num_touch_points == 2) {
                                        this._tmp.x = Math.round(this._tmp.x);
                                        this._tmp.y = Math.round(this._tmp.y);
                                    }
                                    this.grabbed_object.translate(this._tmp.x, this._tmp.y);
                                    this.modified = true;
                                    break;
                                }
                                break;
                            case 3:
                            case 4:
                                if (!this.rotating) {
                                    this._tmp.set(this._last_touch).sub(this.fpos.x, this.fpos.y).mul(0.02f);
                                    this._tmp.mul(camera_speed / 40.0f);
                                    float extra = 1.0f + (2.0f * (this.camera_h.camera_pos.z / 70.0f));
                                    this._tmp.mul(extra);
                                    if (System.currentTimeMillis() - this.last_zoom > 80) {
                                        this.camera_h.add_velocity(this._tmp.x, -this._tmp.y);
                                    }
                                    this._last_touch.set(this.fpos);
                                    break;
                                } else if (!this.undo_begun) {
                                    this.um.begin_step(2, this.grabbed_object);
                                    this.undo_begun = true;
                                    break;
                                }
                                break;
                        }
                    }
                    if (this.rotating && this.grabbed_object != null) {
                        Ray r3 = G.p_cam.getPickRay(x, y);
                        Intersector.intersectRayPlane(r3, yaxis, this.iresult);
                        float current_angle = this.grabbed_object.get_state().angle;
                        float wanted_angle = (float) Math.atan2(this.iresult.y - this.rotate_point.y, this.iresult.x - this.rotate_point.x);
                        if (wanted_angle < 0.0f) {
                            wanted_angle = (float) (wanted_angle + 6.283185307179586d);
                        }
                        int current_rev = (int) (current_angle / 6.283185307179586d);
                        nangle[0] = (float) (wanted_angle + ((current_rev + 2.0f) * 6.283185307179586d));
                        nangle[1] = (float) (wanted_angle + ((current_rev + 1.0f) * 6.283185307179586d));
                        nangle[2] = (float) (wanted_angle + (current_rev * 6.283185307179586d));
                        nangle[3] = (float) (wanted_angle + ((current_rev - 1.0f) * 6.283185307179586d));
                        nangle[4] = (float) (wanted_angle + ((current_rev - 2.0f) * 6.283185307179586d));
                        float new_angle = 0.0f;
                        float last_dist = 100000.0f;
                        for (int i = 0; i < 5; i++) {
                            float d = Math.abs(nangle[i] - current_angle);
                            if (d < last_dist) {
                                last_dist = d;
                                new_angle = nangle[i];
                            }
                        }
                        if (this.num_touch_points == 2) {
                            this.grabbed_object.rotate(new_angle);
                        } else {
                            this.grabbed_object.set_angle(new_angle);
                        }
                        this.modified = true;
                    }
                }
            }
        }
        return true;
    }

    private void handle_rotation() {
    }

    public boolean _touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    private Body raycast_find_body(float screenx, float screeny) {
        Ray r = G.p_cam.getPickRay(screenx, screeny);
        Intersector.intersectRayPlane(r, yaxis, this.iresult);
        this._tmp.set(this.iresult.x, this.iresult.y);
        Intersector.intersectRayPlane(r, this.yaxis2, this.iresult);
        this._tmp2.set(this.iresult.x, this.iresult.y);
        if (sandbox || mode == 3) {
            Intersector.intersectRayPlane(r, this.yaxis3, this.iresult);
            this._tmp3.set(this.iresult.x, this.iresult.y);
            this.query_result_body = null;
            this.query_input_pos = this._tmp3;
            this.query_input_layer = 3;
            world.QueryAABB(this.query_find_object_exact, this._tmp3.x - T_EPSILON, this._tmp3.y - T_EPSILON, this._tmp3.x + T_EPSILON, this._tmp3.y + T_EPSILON);
            if (this.query_result_body != null) {
                Body result = this.query_result_body;
                return result;
            }
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp2;
        this.query_input_layer = 2;
        world.QueryAABB(this.query_find_object_exact, this._tmp2.x - T_EPSILON, this._tmp2.y - T_EPSILON, this._tmp2.x + T_EPSILON, this._tmp2.y + T_EPSILON);
        if (this.query_result_body != null) {
            Body result2 = this.query_result_body;
            return result2;
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp;
        this.query_input_layer = 1;
        world.QueryAABB(this.query_find_object_exact, this._tmp.x - T_EPSILON, this._tmp.y - T_EPSILON, this._tmp.x + T_EPSILON, this._tmp.y + T_EPSILON);
        if (this.query_result_body != null) {
            Body result3 = this.query_result_body;
            return result3;
        }
        if (sandbox) {
            this.query_result_body = null;
            this.query_input_pos = this._tmp3;
            this.query_result_dist2 = 0.0f;
            this.query_input_layer = 3;
            world.QueryAABB(this.query_find_object, this._tmp3.x - T_EPSILON, this._tmp3.y - T_EPSILON, this._tmp3.x + T_EPSILON, this._tmp3.y + T_EPSILON);
            if (this.query_result_body != null) {
                Body result4 = this.query_result_body;
                return result4;
            }
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp2;
        this.query_result_dist2 = 0.0f;
        this.query_input_layer = 2;
        world.QueryAABB(this.query_find_object, this._tmp2.x - T_EPSILON, this._tmp2.y - T_EPSILON, this._tmp2.x + T_EPSILON, this._tmp2.y + T_EPSILON);
        if (this.query_result_body != null) {
            Body result5 = this.query_result_body;
            return result5;
        }
        this.query_result_body = null;
        this.query_input_pos = this._tmp;
        this.query_result_dist2 = 0.0f;
        this.query_input_layer = 1;
        world.QueryAABB(this.query_find_object, this._tmp.x - T_EPSILON, this._tmp.y - T_EPSILON, this._tmp.x + T_EPSILON, this._tmp.y + T_EPSILON);
        if (this.query_result_body == null) {
            return null;
        }
        Body result6 = this.query_result_body;
        return result6;
    }

    private int query_aabb(QueryCallback callback, Vector2 pos, int layer) {
        return query_aabb(callback, pos, T_EPSILON, layer);
    }

    private int query_aabb(QueryCallback callback, Vector2 pos, float epsilon, int layer) {
        this.query_result = -1;
        this.query_result_body = null;
        this.query_result_dist2 = 0.0f;
        this.query_input_pos = pos;
        this.query_input_layer = layer;
        world.QueryAABB(callback, pos.x - T_EPSILON, pos.y - T_EPSILON, pos.x + T_EPSILON, T_EPSILON + pos.y);
        return this.query_result;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int arg0) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean mouseMoved(int x, int y) {
        this.fpos.set(x, y);
        return true;
    }

    @Override // com.bithack.apparatus.ui.WidgetValueCallback
    public void on_widget_value_change(int id, float value) {
        float f;
        switch (id) {
            case 0:
                if (this.grabbed_object != null) {
                    if (this.grabbed_object instanceof Bar) {
                        ((Bar) this.grabbed_object).size.x = 2 << ((int) (1.0f + value));
                        remove_potential_hinges(this.grabbed_object, true, true);
                        ((Bar) this.grabbed_object).reshape();
                        break;
                    } else if (this.grabbed_object instanceof Wheel) {
                        Wheel wheel = (Wheel) this.grabbed_object;
                        if (value < -0.5f) {
                            f = 0.5f;
                        } else {
                            f = value < 0.5f ? 1 : 2;
                        }
                        wheel.size = f;
                        remove_potential_hinges(this.grabbed_object, true, true);
                        ((Wheel) this.grabbed_object).reshape();
                        break;
                    } else if ((this.grabbed_object instanceof RopeEnd) || (this.grabbed_object instanceof PanelCableEnd) || (this.grabbed_object instanceof CableEnd)) {
                        if (this.grabbed_object instanceof CableEnd) {
                            ((CableEnd) this.grabbed_object).cable.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                            break;
                        } else if (this.grabbed_object instanceof PanelCableEnd) {
                            ((PanelCableEnd) this.grabbed_object).cable.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                            break;
                        } else if (this.grabbed_object instanceof RopeEnd) {
                            ((RopeEnd) this.grabbed_object).rope.set_size(((value + 1.0f) * 5.0f) + 5.0f);
                            break;
                        }
                    }
                }
                break;
            case 1:
                if (this.grabbed_object instanceof Battery) {
                    ((Battery) this.grabbed_object).voltage = (value + 1.0f) / 2.0f;
                    break;
                }
                break;
            case 2:
                if (this.grabbed_object instanceof Battery) {
                    ((Battery) this.grabbed_object).current = (value + 1.0f) / 2.0f;
                    break;
                }
                break;
            case 6:
                if (this.grabbed_object instanceof Battery) {
                    int v = (int) value;
                    switch (v) {
                        case -1:
                            if (1 != ((Battery) this.grabbed_object).size) {
                                ((Battery) this.grabbed_object).resize(1);
                                break;
                            }
                            break;
                        case 0:
                            this.widget_sizeb.value = ((((Battery) this.grabbed_object).size - 1) * 2) - 1;
                            break;
                        case 1:
                            if (2 != ((Battery) this.grabbed_object).size) {
                                ((Battery) this.grabbed_object).resize(2);
                                break;
                            }
                            break;
                    }
                }
                break;
            case 7:
                if (this.grabbed_object instanceof RopeEnd) {
                    int v2 = (int) value;
                    Rope rope = ((RopeEnd) this.grabbed_object).rope;
                    switch (v2) {
                        case -1:
                            rope.set_elastic(false);
                            break;
                        case 0:
                            this.widget_elasticity.value = rope.rubber ? 1 : -1;
                            break;
                        case 1:
                            rope.set_elastic(true);
                            break;
                    }
                }
                break;
            case 8:
                if (this.grabbed_object instanceof RocketEngine) {
                    RocketEngine e = (RocketEngine) this.grabbed_object;
                    e.thrust = (value + 1.0f) / 2.0f;
                    break;
                }
                break;
            case 9:
                if (this.grabbed_object instanceof DamperEnd) {
                    ((DamperEnd) this.grabbed_object).damper.speed = ((value + 1.0f) / 2.0f) * 200.0f;
                    ((DamperEnd) this.grabbed_object).damper.update_motor();
                    break;
                }
                break;
            case 10:
                if (this.grabbed_object instanceof DamperEnd) {
                    ((DamperEnd) this.grabbed_object).damper.force = ((value + 1.0f) / 2.0f) * 1000.0f;
                    ((DamperEnd) this.grabbed_object).damper.update_motor();
                    break;
                }
                break;
        }
    }

    public void begin_challenge_testing() {
        from_sandbox = true;
        sandbox = false;
        this.um.clear();
        create_metal_cache();
        Iterator<GrabableObject> it = this.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject b = it.next();
            if (b.num_hinges > 0) {
                b.fixed_dynamic = true;
            }
        }
        if (this.grabbed_object != null) {
            release_object();
        }
        this.contact_handler.reset();
        Iterator<GrabableObject> it2 = this.om.all.iterator();
        while (it2.hasNext()) {
            GrabableObject o = it2.next();
            o.sandbox_save();
        }
    }

    public void end_challenge_testing() {
        if (from_sandbox) {
            this.um.clear();
            sandbox = true;
            from_sandbox = false;
            world.setContactFilter(this.falsefilter);
            this.contact_handler.reset();
            Iterator<GrabableObject> it = this.om.all.iterator();
            while (it.hasNext()) {
                GrabableObject b = it.next();
                if (b.num_hinges > 0) {
                    b.fixed_dynamic = false;
                }
            }
            if (this.grabbed_object != null) {
                release_object();
            }
            Iterator<GrabableObject> it2 = this.om.all.iterator();
            while (it2.hasNext()) {
                GrabableObject o = it2.next();
                o.set_active(false);
            }
            Iterator<Hinge> i = this.hinges.iterator();
            while (i.hasNext()) {
                Hinge h = i.next();
                if (!h.fixed) {
                    h.dispose(world);
                    i.remove();
                }
            }
            Iterator<DynamicMotor> i2 = this.om.layer0.dynamicmotors.iterator();
            while (i2.hasNext()) {
                DynamicMotor h2 = i2.next();
                if (!h2.fixed) {
                    h2.detach();
                }
            }
            Iterator<DynamicMotor> i3 = this.om.layer0.dynamicmotors.iterator();
            while (i3.hasNext()) {
                DynamicMotor h3 = i3.next();
                if (!h3.fixed) {
                    h3.detach();
                }
            }
            Iterator<StaticMotor> i4 = this.om.static_motors.iterator();
            while (i4.hasNext()) {
                StaticMotor h4 = i4.next();
                if (!h4.fixed) {
                    h4.detach();
                }
            }
            Iterator<GrabableObject> it3 = this.om.all.iterator();
            while (it3.hasNext()) {
                GrabableObject o2 = it3.next();
                o2.set_active(false);
            }
            Iterator<GrabableObject> it4 = this.om.all.iterator();
            while (it4.hasNext()) {
                GrabableObject o3 = it4.next();
                o3.sandbox_load();
            }
            Iterator<GrabableObject> it5 = this.om.all.iterator();
            while (it5.hasNext()) {
                GrabableObject o4 = it5.next();
                o4.set_active(true);
            }
            world.setContactFilter(this.contact_handler);
        }
    }

    public void add_corner(MetalCorner a) {
        this.om.add(a);
    }

    public void remove_corner(MetalCorner a) {
        this.om.remove(a);
    }

    private class SimpleContactHandler implements ContactListener, ContactFilter {
        private SimpleContactHandler() {
        }

        /* synthetic */ SimpleContactHandler(Game game, SimpleContactHandler simpleContactHandler) {
            this();
        }

        @Override // com.badlogic.gdx.physics.box2d.ContactFilter
        public boolean shouldCollide(Fixture f1, Fixture f2) {
            Body A = f1.getBody();
            Body B = f2.getBody();
            if (A.getUserData() == null || B.getUserData() == null) {
                return false;
            }
            if (f1.isSensor() || f2.isSensor()) {
                return true;
            }
            BaseObject objA = (BaseObject) A.getUserData();
            BaseObject objB = (BaseObject) B.getUserData();
            return objA.layer == objB.layer;
        }

        @Override // com.badlogic.gdx.physics.box2d.ContactListener
        public void beginContact(Contact c) {
            if (Game.mode == 3) {
                if ((c.getFixtureA().getBody().getUserData() instanceof Mine) && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 1.8f) {
                    ((Mine) c.getFixtureA().getBody().getUserData()).trigger(Game.world);
                }
                if ((c.getFixtureB().getBody().getUserData() instanceof Mine) && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 1.8f) {
                    ((Mine) c.getFixtureB().getBody().getUserData()).trigger(Game.world);
                }
                if ((c.getFixtureA().getUserData() instanceof Battery) && System.currentTimeMillis() - Game.this.game_start > 300 && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Battery) c.getFixtureA().getUserData()).toggle_onoff();
                }
                if ((c.getFixtureB().getUserData() instanceof Battery) && System.currentTimeMillis() - Game.this.game_start > 300 && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Battery) c.getFixtureB().getUserData()).toggle_onoff();
                }
                if ((c.getFixtureA().getUserData() instanceof Button) && System.currentTimeMillis() - Game.this.game_start > 50 && !c.getFixtureB().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Button) c.getFixtureA().getUserData()).activate();
                }
                if ((c.getFixtureB().getUserData() instanceof Button) && System.currentTimeMillis() - Game.this.game_start > 50 && !c.getFixtureA().isSensor() && c.getFixtureA().getBody().getLinearVelocity().dst(c.getFixtureB().getBody().getLinearVelocity()) > 5.0f) {
                    ((Button) c.getFixtureB().getUserData()).activate();
                }
                Fixture a = c.getFixtureA();
                Fixture b = c.getFixtureB();
                Object objA = a.getBody().getUserData();
                Object objB = b.getBody().getUserData();
                if (a.isSensor()) {
                    if ((objA instanceof Bucket) && ((objB instanceof Marble) || (objB instanceof ChristmasGift))) {
                        Game.this.num_balls_in_goal++;
                    }
                } else if (b.isSensor() && (objB instanceof Bucket) && ((objA instanceof Marble) || (objA instanceof ChristmasGift))) {
                    Game.this.num_balls_in_goal++;
                }
                if (objA instanceof ChristmasGift) {
                    SoundManager.handle_gift_hit((ChristmasGift) objA, (GrabableObject) objB);
                    return;
                }
                if (objA instanceof Marble) {
                    SoundManager.handle_marble_hit((Marble) objA, (GrabableObject) objB);
                    return;
                }
                if (objA instanceof MetalWheel) {
                    SoundManager.handle_metal_hit((MetalWheel) objA, (GrabableObject) objB);
                    return;
                }
                if ((objA instanceof Plank) || (objA instanceof Wheel)) {
                    SoundManager.handle_wood_hit((GrabableObject) objA, (GrabableObject) objB);
                    return;
                }
                if (objB instanceof Marble) {
                    SoundManager.handle_marble_hit((Marble) objB, (GrabableObject) objA);
                    return;
                }
                if (objB instanceof MetalWheel) {
                    SoundManager.handle_metal_hit((MetalWheel) objB, (GrabableObject) objA);
                } else if ((objB instanceof Plank) || (objB instanceof Wheel)) {
                    SoundManager.handle_wood_hit((GrabableObject) objB, (GrabableObject) objA);
                }
            }
        }

        @Override // com.badlogic.gdx.physics.box2d.ContactListener
        public void endContact(Contact c) {
            Fixture a = c.getFixtureA();
            Fixture b = c.getFixtureB();
            if (a != null && b != null && a.getBody() != null && b.getBody() != null) {
                if (a.getBody().getUserData() instanceof Marble) {
                    SoundManager.stop_marble_roll((Marble) a.getBody().getUserData());
                }
                if (b.getBody().getUserData() instanceof Marble) {
                    SoundManager.stop_marble_roll((Marble) b.getBody().getUserData());
                }
                Object objA = a.getBody().getUserData();
                Object objB = b.getBody().getUserData();
                if (a.isSensor()) {
                    if (objA instanceof Bucket) {
                        if ((objB instanceof Marble) || (objB instanceof ChristmasGift)) {
                            Game game = Game.this;
                            game.num_balls_in_goal--;
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (b.isSensor() && (objB instanceof Bucket)) {
                    if ((objA instanceof Marble) || (objA instanceof ChristmasGift)) {
                        Game game2 = Game.this;
                        game2.num_balls_in_goal--;
                    }
                }
            }
        }

        @Override // com.badlogic.gdx.physics.box2d.ContactListener
        public void postSolve(Contact arg0, ContactImpulse arg1) {
        }

        @Override // com.badlogic.gdx.physics.box2d.ContactListener
        public void preSolve(Contact arg0, Manifold arg1) {
        }
    }

    public static class ConnectAnim {
        int dir;
        float size;
        float x;
        float y;

        public ConnectAnim(int dir, float x, float y) {
            this.size = 0.0f;
            if (dir == 0) {
                this.size = 1.0f;
            } else {
                this.size = 0.0f;
            }
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        public boolean render() {
            if (this.dir == 0) {
                this.size -= G.delta * 4.0f;
                if (this.size <= 0.0f) {
                    return false;
                }
            } else {
                this.size += G.delta * 6.0f;
                if (this.size >= 1.0f) {
                    return false;
                }
            }
            G.gl.glPushMatrix();
            G.gl.glTranslatef(this.x, this.y, 1.0f);
            G.gl.glScalef(this.size * 2.0f, this.size * 2.0f, 1.0f);
            MiscRenderer.draw_colored_circle();
            G.gl.glPopMatrix();
            return true;
        }
    }

    private class SimulationThread extends Thread {
        boolean modified;
        private long t_accum;
        private long t_delta;
        private long t_last;
        private long t_now;
        boolean terminate;

        private SimulationThread() {
            this.terminate = false;
            this.modified = false;
        }

        /* synthetic */ SimulationThread(Game game, SimulationThread simulationThread) {
            this();
        }

        public int swap_state_buffers() {
            synchronized (this) {
                if (this.modified) {
                    this.modified = false;
                }
            }
            return 0;
        }

        public void terminate() {
            synchronized (this) {
                this.terminate = true;
            }
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:52:0x009a A[SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() throws InterruptedException {
            int iterations;
            Gdx.app.log("SIMULATION THREAD", "STARTED");
            this.t_last = System.nanoTime();
            this.t_accum = 0L;
            while (true) {
                boolean i = false;
                this.t_now = System.nanoTime();
                if (this.t_accum > 100000000) {
                    this.t_accum = 24000000L;
                }
                this.t_delta = (this.t_now - this.t_last) + this.t_accum;
                this.t_last = this.t_now;
                if (this.t_delta > 100000000) {
                    this.t_delta = 48000000L;
                }
                if (this.t_delta >= 8000000) {
                    i = true;
                    do {
                        if (Game.physics_stability == 1) {
                            iterations = 10;
                        } else {
                            iterations = Game.physics_stability == 0 ? 1 : 64;
                        }
                        Game.world.step(0.011f, iterations, iterations);
                        Iterator<Hinge> it = Game.this.hinges.iterator();
                        while (it.hasNext()) {
                            Hinge h = it.next();
                            h.tick();
                        }
                        this.t_delta -= 8000000;
                    } while (this.t_delta >= 8000000);
                }
                this.t_accum = this.t_delta;
                synchronized (this) {
                    if (i) {
                        Iterator<GrabableObject> it2 = Game.this.om.all.iterator();
                        while (it2.hasNext()) {
                            BaseObject o = it2.next();
                            o.save_state();
                        }
                        this.modified = true;
                        if (!this.terminate) {
                            Gdx.app.log("SIMULATION THREAD", "TERMINATED");
                            return;
                        }
                    } else if (!this.terminate) {
                    }
                }
                long sleep = Math.max(0L, 8 - ((System.nanoTime() - this.t_now) / 1000000));
                if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void set_bg(int x) {
        if (this.background_n != x) {
            if (bgtexlow != null && bgtexlow != bgtex) {
                TextureFactory.unload(bgtexlow);
            }
            if (bgtex != null) {
                TextureFactory.unload(bgtex);
            }
            bgtexlow = null;
            bgtex = null;
            if (x <= 0) {
                if (G.realwidth <= 870 && G.realwidth < 600) {
                    bgtex = TextureFactory.load_mipmapped("data/bg_low.jpg");
                    bgtexlow = TextureFactory.load_mipmapped("data/bg_low.jpg");
                } else {
                    bgtex = TextureFactory.load_mipmapped("data/bg_medium.jpg");
                    bgtexlow = TextureFactory.load_mipmapped("data/bg_low.jpg");
                }
            } else {
                bgtex = TextureFactory.load_mipmapped("data/bg" + x + ".jpg");
                bgtexlow = bgtex;
            }
            this.background_n = x;
            if (this.level != null) {
                this.level.background = this.background_n;
            }
        }
    }
}
