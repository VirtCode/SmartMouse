package ch.virt.smartphonemouse.transmission.hid;

/**
 * This class holds the Hid Descriptor used for this app.
 */
public class HidDescriptor {

    // Tag IDs
    private static final byte TAG_USAGE_PAGE = 0x05;
    private static final byte TAG_USAGE = 0x09;
    private static final byte TAG_COLLECTION = (byte) 0xA1;
    private static final byte TAG_USAGE_MIN = 0x19;
    private static final byte TAG_USAGE_MAX = 0x29;
    private static final byte TAG_LOGICAL_MIN = 0x15;
    private static final byte TAG_LOGICAL_MAX = 0x25;
    private static final byte TAG_LOGICAL_MAX_S = 0x26;
    private static final byte TAG_PHYSICAL_MIN = 0x35;
    private static final byte TAG_PHYSICAL_MAX = 0x45;
    private static final byte TAG_PHYSICAL_MAX_S = 0x46;
    private static final byte TAG_REPORT_ID = (byte) 0x85;
    private static final byte TAG_REPORT_COUNT = (byte) 0x95;
    private static final byte TAG_REPORT_SIZE = 0x75;
    private static final byte TAG_INPUT = (byte) 0x81;
    private static final byte TAG_END_COLLECTION = (byte) 0xC0;
    private static final byte TAG_UNIT_EXPONENT = 0x55;
    private static final byte TAG_UNIT = 0x65;

    // Units
    private static final byte SI_CENTIMETER = 0x11;

    // Usage Pages (HUT 1.5)
    private static final byte UP_GENERIC_DESKTOP = 0x01;
    private static final byte UP_DIGITIZER = 0x0D;
    private static final byte UP_BUTTON = 0x09;

    // Usages (HUT 1.5)
    // (Generic Desktop)
    private static final byte U_GD_MOUSE = 0x02;
    private static final byte U_GD_POINTER = 0x01;
    private static final byte U_GD_X = 0x30;
    private static final byte U_GD_Y = 0x31;
    private static final byte U_GD_WHEEL = 0x38;
    // (Digitizer)
    private static final byte U_DI_TOUCHPAD = 0x05;
    private static final byte U_DI_FINGER = 0x22;
    private static final byte U_DI_TIP_SWITCH = 0x42;
    private static final byte U_DI_TOUCH_VALID = 0x47;
    private static final byte U_DI_CONTACT_IDENTIFIER = 0x51;

    // Collections (HID 1.11, 6.2.2.4)
    private static final byte C_PHYSICAL = 0x00;
    private static final byte C_APPLICATION = 0x01;
    private static final byte C_LOGICAL = 0x02;

    // Input Flags (HID 1.11, 6.2.2.4)
    private static final byte I_DATA = 0b00000000;
    private static final byte I_CONSTANT = 0b00000001;

    private static final byte I_ARRAY = 0b00000000;
    private static final byte I_VARIABLE = 0b00000010;

    private static final byte I_ABSOLUTE = 0b00000000;
    private static final byte I_RELATIVE = 0b00000100;

    public static final int REPORT_MOUSE = 1;
    public static final int REPORT_TOUCHPAD = 2;

    // Descriptor
    public static final byte[] DESCRIPTOR = new byte[] {


            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_TOUCHPAD,
            TAG_COLLECTION,     C_APPLICATION,

                // Normal Mouse
                TAG_REPORT_ID,      REPORT_MOUSE,

                TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                TAG_USAGE,          U_GD_POINTER,
                TAG_COLLECTION,     C_PHYSICAL,

                    // Mouse Buttons
                    TAG_USAGE_PAGE,     UP_BUTTON,
                    TAG_USAGE_MIN,      1,
                    TAG_USAGE_MAX,      3,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX,    1,
                    TAG_REPORT_SIZE,    1,
                    TAG_REPORT_COUNT,   3,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Padding
                    TAG_REPORT_SIZE,    5,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_CONSTANT,

                    // X, Y Axis, Wheel
                    TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                    TAG_USAGE,          U_GD_X,
                    TAG_USAGE,          U_GD_Y,
                    TAG_USAGE,          U_GD_WHEEL,
                    TAG_LOGICAL_MIN,    -127,
                    TAG_LOGICAL_MAX,     127,
                    TAG_REPORT_SIZE,    8,
                    TAG_REPORT_COUNT,   3,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_RELATIVE,

                TAG_END_COLLECTION,

                // Touchpad
                TAG_REPORT_ID,      REPORT_TOUCHPAD,

                // Finger 1
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_FINGER,
                TAG_COLLECTION,     C_LOGICAL,

                    // Actually Touching
                    TAG_USAGE_PAGE,     UP_DIGITIZER,
                    TAG_USAGE,          U_DI_TOUCH_VALID,
                    TAG_USAGE,          U_DI_TIP_SWITCH,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX,    1,
                    TAG_REPORT_SIZE,    1,
                    TAG_REPORT_COUNT,   2,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Contact Identifier
                    TAG_USAGE_PAGE,     UP_DIGITIZER,
                    TAG_USAGE,          U_DI_CONTACT_IDENTIFIER,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX,    10,
                    TAG_REPORT_SIZE,    4,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Padding
                    TAG_REPORT_SIZE,    2,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_CONSTANT,

                    // Position
                    TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX_S,  0x00, 0x10, // 4096

                    TAG_UNIT_EXPONENT,  0,
                    TAG_UNIT,           SI_CENTIMETER,
                    TAG_PHYSICAL_MIN,   0,
                    TAG_PHYSICAL_MAX,   20, // 20 cm

                    TAG_REPORT_SIZE,    16,
                    TAG_REPORT_COUNT,   1,

                    TAG_USAGE,          U_GD_X,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    TAG_USAGE,          U_GD_Y,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_END_COLLECTION,

                // Finger 2
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_FINGER,
                TAG_COLLECTION,     C_LOGICAL,

                    // Actually Touching
                    TAG_USAGE_PAGE,     UP_DIGITIZER,
                    TAG_USAGE,          U_DI_TOUCH_VALID,
                    TAG_USAGE,          U_DI_TIP_SWITCH,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX,    1,
                    TAG_REPORT_SIZE,    1,
                    TAG_REPORT_COUNT,   2,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Contact Identifier
                    TAG_USAGE_PAGE,     UP_DIGITIZER,
                    TAG_USAGE,          U_DI_CONTACT_IDENTIFIER,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX,    10,
                    TAG_REPORT_SIZE,    4,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Padding
                    TAG_REPORT_SIZE,    2,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_CONSTANT,

                    // Position
                    TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX_S,  0x00, 0x10, // 4096

                    TAG_UNIT_EXPONENT,  0,
                    TAG_UNIT,           SI_CENTIMETER,
                    TAG_PHYSICAL_MIN,   0,
                    TAG_PHYSICAL_MAX,   20, // 20 cm

                    TAG_REPORT_SIZE,    16,
                    TAG_REPORT_COUNT,   1,

                    TAG_USAGE,          U_GD_X,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    TAG_USAGE,          U_GD_Y,
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_END_COLLECTION,

            // Finger 3
            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_FINGER,
            TAG_COLLECTION,     C_LOGICAL,

                // Actually Touching
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_TOUCH_VALID,
                TAG_USAGE,          U_DI_TIP_SWITCH,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    1,
                TAG_REPORT_SIZE,    1,
                TAG_REPORT_COUNT,   2,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Contact Identifier
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_CONTACT_IDENTIFIER,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    10,
                TAG_REPORT_SIZE,    4,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Padding
                TAG_REPORT_SIZE,    2,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT,          I_CONSTANT,

                // Position
                TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX_S,  0x00, 0x10, // 4096

                TAG_UNIT_EXPONENT,  0,
                TAG_UNIT,           SI_CENTIMETER,
                TAG_PHYSICAL_MIN,   0,
                TAG_PHYSICAL_MAX,   20, // 20 cm

                TAG_REPORT_SIZE,    16,
                TAG_REPORT_COUNT,   1,

                TAG_USAGE,          U_GD_X,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_USAGE,          U_GD_Y,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

            TAG_END_COLLECTION,

            // Finger 4
            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_FINGER,
            TAG_COLLECTION,     C_LOGICAL,

                // Actually Touching
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_TOUCH_VALID,
                TAG_USAGE,          U_DI_TIP_SWITCH,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    1,
                TAG_REPORT_SIZE,    1,
                TAG_REPORT_COUNT,   2,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Contact Identifier
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_CONTACT_IDENTIFIER,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    10,
                TAG_REPORT_SIZE,    4,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Padding
                TAG_REPORT_SIZE,    2,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT,          I_CONSTANT,

                // Position
                TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX_S,  0x00, 0x10, // 4096

                TAG_UNIT_EXPONENT,  0,
                TAG_UNIT,           SI_CENTIMETER,
                TAG_PHYSICAL_MIN,   0,
                TAG_PHYSICAL_MAX,   20, // 20 cm

                TAG_REPORT_SIZE,    16,
                TAG_REPORT_COUNT,   1,

                TAG_USAGE,          U_GD_X,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_USAGE,          U_GD_Y,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

            TAG_END_COLLECTION,

            // Finger 5
            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_FINGER,
            TAG_COLLECTION,     C_LOGICAL,

                // Actually Touching
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_TOUCH_VALID,
                TAG_USAGE,          U_DI_TIP_SWITCH,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    1,
                TAG_REPORT_SIZE,    1,
                TAG_REPORT_COUNT,   2,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Contact Identifier
                TAG_USAGE_PAGE,     UP_DIGITIZER,
                TAG_USAGE,          U_DI_CONTACT_IDENTIFIER,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX,    10,
                TAG_REPORT_SIZE,    4,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                // Padding
                TAG_REPORT_SIZE,    2,
                TAG_REPORT_COUNT,   1,
                TAG_INPUT,          I_CONSTANT,

                // Position
                TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                TAG_LOGICAL_MIN,    0,
                TAG_LOGICAL_MAX_S,  0x00, 0x10, // 4096

                TAG_UNIT_EXPONENT,  0,
                TAG_UNIT,           SI_CENTIMETER,
                TAG_PHYSICAL_MIN,   0,
                TAG_PHYSICAL_MAX,   20, // 20 cm

                TAG_REPORT_SIZE,    16,
                TAG_REPORT_COUNT,   1,

                TAG_USAGE,          U_GD_X,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_USAGE,          U_GD_Y,
                TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

            TAG_END_COLLECTION,

        TAG_END_COLLECTION,
    };
}
