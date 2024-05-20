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
            // Normal Mouse
            TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
            TAG_USAGE,          U_GD_MOUSE,
            TAG_COLLECTION,     C_APPLICATION,

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

            TAG_END_COLLECTION,

            // Touchpad
            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_TOUCHPAD,
            TAG_COLLECTION,     C_APPLICATION,

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
                    TAG_LOGICAL_MAX_S,  (byte) 0x00, 0x10, // 4096

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
                    TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Padding
                    TAG_REPORT_SIZE,    2,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_CONSTANT,

                    // Position
                    TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                    TAG_LOGICAL_MIN,    0,
                    TAG_LOGICAL_MAX_S,  (byte) 0x00, 0x10, // 4096

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
/*
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
                    TAG_LOGICAL_MAX_S,  (byte) 0x00, 0x10, // 4096

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
 */

            TAG_END_COLLECTION

    };

    public static final byte[] DESCRIPTOR_TOUCHPAD = {
            // Touchpad
            TAG_USAGE_PAGE,     UP_DIGITIZER,
            TAG_USAGE,          U_DI_TOUCHPAD,
            TAG_COLLECTION,     C_APPLICATION,

                TAG_REPORT_ID,      2,

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
                    TAG_USAGE_PAGE, UP_DIGITIZER,
                    TAG_USAGE, U_DI_CONTACT_IDENTIFIER,
                    TAG_LOGICAL_MIN, 0,
                    TAG_LOGICAL_MAX, 2,
                    TAG_REPORT_SIZE, 2,
                    TAG_REPORT_COUNT, 1,
                    TAG_INPUT, I_DATA | I_VARIABLE | I_ABSOLUTE,

                    // Padding
                    TAG_REPORT_SIZE,    4,
                    TAG_REPORT_COUNT,   1,
                    TAG_INPUT,          I_CONSTANT,

                    // Position
                    TAG_USAGE_PAGE,     UP_GENERIC_DESKTOP,
                    TAG_LOGICAL_MIN,    0,
                    // maybe specify physical min here (and max later down ofc)
                    TAG_REPORT_SIZE,    16,
                    TAG_REPORT_COUNT,   1,
                    // also specify unit?

                    TAG_USAGE,          U_GD_X,
                    TAG_LOGICAL_MAX_S,  (byte) 0xE8, 0x03, // 1000
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                    TAG_USAGE,          U_GD_Y,
                    TAG_LOGICAL_MAX_S,  (byte) 0xF4, 0x01, // 500
                    TAG_INPUT,          I_DATA | I_VARIABLE | I_ABSOLUTE,

                TAG_END_COLLECTION,

            TAG_END_COLLECTION
    };

    // also ms precision touch device but with functions removed (bare minimum for linux kernel)
    public static final byte[] DESCRIPTOR_B_STILL_WORKING = {
            0x05, 0x0d,                         // USAGE_PAGE (Digitizers)
            0x09, 0x05,                         // USAGE (Touch Pad)
            (byte) 0xa1, 0x01,                         // COLLECTION (Application)
            (byte) 0x85, 2,            //   REPORT_ID (Touch pad)
            0x09, 0x22,                         //   USAGE (Finger)
            (byte) 0xa1, 0x02,                         //   COLLECTION (Logical)
            0x15, 0x00,                         //       LOGICAL_MINIMUM (0)
            0x25, 0x01,                         //       LOGICAL_MAXIMUM (1)
            0x09, 0x47,                         //       USAGE (Confidence)
            0x09, 0x42,                         //       USAGE (Tip switch)
            (byte) 0x95, 0x02,                         //       REPORT_COUNT (2)
            0x75, 0x01,                         //       REPORT_SIZE (1)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            (byte) 0x95, 0x01,                         //       REPORT_COUNT (1)
            0x75, 0x02,                         //       REPORT_SIZE (2)
            0x25, 0x02,                         //       LOGICAL_MAXIMUM (2)
            0x09, 0x51,                         //       USAGE (Contact Identifier)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            0x75, 0x01,                         //       REPORT_SIZE (1)
            (byte) 0x95, 0x04,                         //       REPORT_COUNT (4)
            (byte) 0x81, 0x03,                         //       INPUT (Cnst,Var,Abs)
            0x05, 0x01,                         //       USAGE_PAGE (Generic Desk..
            0x15, 0x00,                         //       LOGICAL_MINIMUM (0)
            0x26, (byte) 0xf4, 0x03,                   //       LOGICAL_MAXIMUM (4095)
            0x75, 0x10,                         //       REPORT_SIZE (16)
            0x55, 0x0e,                         //       UNIT_EXPONENT (-2)
            0x65, 0x13,                         //       UNIT(Inch,EngLinear)
            0x09, 0x30,                         //       USAGE (X)
            0x35, 0x00,                         //       PHYSICAL_MINIMUM (0)
            0x46, (byte) 0x90, 0x01,                   //       PHYSICAL_MAXIMUM (400)
            (byte) 0x95, 0x01,                         //       REPORT_COUNT (1)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            0x46, 0x13, 0x01,                   //       PHYSICAL_MAXIMUM (275)
            0x09, 0x31,                         //       USAGE (Y)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            (byte) 0xc0,                               //    END_COLLECTION
            /*
            0x55, 0x0C,                         //    UNIT_EXPONENT (-4)
            0x66, 0x01, 0x10,                   //    UNIT (Seconds)
            0x47, (byte) 0xff, (byte) 0xff, 0x00, 0x00,      //     PHYSICAL_MAXIMUM (65535)
            0x27, (byte) 0xff,(byte)  0xff, 0x00, 0x00,         //  LOGICAL_MAXIMUM (65535)
            0x75, 0x10,                           //  REPORT_SIZE (16)
            (byte) 0x95, 0x01,                           //  REPORT_COUNT (1)
            0x05, 0x0d,                         //    USAGE_PAGE (Digitizers)
            0x09, 0x56,                         //    USAGE (Scan Time)
            (byte) 0x81, 0x02,                           //  INPUT (Data,Var,Abs)
            0x09, 0x54,                         //    USAGE (Contact count)
            0x25, 0x7f,                           //  LOGICAL_MAXIMUM (127)
            (byte) 0x95, 0x01,                         //    REPORT_COUNT (1)
            0x75, 0x08,                         //    REPORT_SIZE (8)
            (byte) 0x81, 0x02,                         //    INPUT (Data,Var,Abs)
            0x05, 0x09,                         //    USAGE_PAGE (Button)
            0x09, 0x01,                         //    USAGE_(Button 1)
            0x09, 0x02,                         //    USAGE_(Button 2)
            0x09, 0x03,                         //    USAGE_(Button 3)
            0x25, 0x01,                         //    LOGICAL_MAXIMUM (1)
            0x75, 0x01,                         //    REPORT_SIZE (1)
            (byte) 0x95, 0x03,                         //    REPORT_COUNT (3)
            (byte) 0x81, 0x02,                         //    INPUT (Data,Var,Abs)
            (byte) 0x95, 0x05,                          //   REPORT_COUNT (5)
            (byte) 0x81, 0x03,                         //    INPUT (Cnst,Var,Abs)
            0x05, 0x0d,                         //    USAGE_PAGE (Digitizer)
            (byte) 0x85, 1,            //   REPORT_ID (Feature)
            0x09, 0x55,                         //    USAGE (Contact Count Maximum)
            0x09, 0x59,                         //    USAGE (Pad TYpe)
            0x75, 0x04,                         //    REPORT_SIZE (4)
            (byte) 0x95, 0x02,                         //    REPORT_COUNT (2)
            0x25, 0x0f,                         //    LOGICAL_MAXIMUM (15)
            (byte) 0xb1, 0x02,                         //    FEATURE (Data,Var,Abs)
             */
            (byte) 0xc0,                               // END_COLLECTION
    };

    // digitizer section from ms precision touch device
    public static final byte[] DESCRIPTOR_WORKING_DEVICE_TYPE = {
            0x05, 0x0d,                         // USAGE_PAGE (Digitizers)
            0x09, 0x05,                         // USAGE (Touch Pad)
            (byte) 0xa1, 0x01,                         // COLLECTION (Application)
            (byte) 0x85, 2,            //   REPORT_ID (Touch pad)
            0x09, 0x22,                         //   USAGE (Finger)
            (byte) 0xa1, 0x02,                         //   COLLECTION (Logical)
            0x15, 0x00,                         //       LOGICAL_MINIMUM (0)
            0x25, 0x01,                         //       LOGICAL_MAXIMUM (1)
            0x09, 0x47,                         //       USAGE (Confidence)
            0x09, 0x42,                         //       USAGE (Tip switch)
            (byte) 0x95, 0x02,                         //       REPORT_COUNT (2)
            0x75, 0x01,                         //       REPORT_SIZE (1)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            (byte) 0x95, 0x01,                         //       REPORT_COUNT (1)
            0x75, 0x02,                         //       REPORT_SIZE (2)
            0x25, 0x02,                         //       LOGICAL_MAXIMUM (2)
            0x09, 0x51,                         //       USAGE (Contact Identifier)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            0x75, 0x01,                         //       REPORT_SIZE (1)
            (byte) 0x95, 0x04,                         //       REPORT_COUNT (4)
            (byte) 0x81, 0x03,                         //       INPUT (Cnst,Var,Abs)
            0x05, 0x01,                         //       USAGE_PAGE (Generic Desk..
            0x15, 0x00,                         //       LOGICAL_MINIMUM (0)
            0x26, (byte) 0xff, 0x0f,                   //       LOGICAL_MAXIMUM (4095)
            0x75, 0x10,                         //       REPORT_SIZE (16)
            0x55, 0x0e,                         //       UNIT_EXPONENT (-2)
            0x65, 0x13,                         //       UNIT(Inch,EngLinear)
            0x09, 0x30,                         //       USAGE (X)
            0x35, 0x00,                         //       PHYSICAL_MINIMUM (0)
            0x46, (byte) 0x90, 0x01,                   //       PHYSICAL_MAXIMUM (400)
            (byte) 0x95, 0x01,                         //       REPORT_COUNT (1)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            0x46, 0x13, 0x01,                   //       PHYSICAL_MAXIMUM (275)
            0x09, 0x31,                         //       USAGE (Y)
            (byte) 0x81, 0x02,                         //       INPUT (Data,Var,Abs)
            (byte) 0xc0,                               //    END_COLLECTION
            0x55, 0x0C,                         //    UNIT_EXPONENT (-4)
            0x66, 0x01, 0x10,                   //    UNIT (Seconds)
            0x47, (byte) 0xff, (byte) 0xff, 0x00, 0x00,      //     PHYSICAL_MAXIMUM (65535)
            0x27, (byte) 0xff,(byte)  0xff, 0x00, 0x00,         //  LOGICAL_MAXIMUM (65535)
            0x75, 0x10,                           //  REPORT_SIZE (16)
            (byte) 0x95, 0x01,                           //  REPORT_COUNT (1)
            0x05, 0x0d,                         //    USAGE_PAGE (Digitizers)
            0x09, 0x56,                         //    USAGE (Scan Time)
            (byte) 0x81, 0x02,                           //  INPUT (Data,Var,Abs)
            0x09, 0x54,                         //    USAGE (Contact count)
            0x25, 0x7f,                           //  LOGICAL_MAXIMUM (127)
            (byte) 0x95, 0x01,                         //    REPORT_COUNT (1)
            0x75, 0x08,                         //    REPORT_SIZE (8)
            (byte) 0x81, 0x02,                         //    INPUT (Data,Var,Abs)
            0x05, 0x09,                         //    USAGE_PAGE (Button)
            0x09, 0x01,                         //    USAGE_(Button 1)
            0x09, 0x02,                         //    USAGE_(Button 2)
            0x09, 0x03,                         //    USAGE_(Button 3)
            0x25, 0x01,                         //    LOGICAL_MAXIMUM (1)
            0x75, 0x01,                         //    REPORT_SIZE (1)
            (byte) 0x95, 0x03,                         //    REPORT_COUNT (3)
            (byte) 0x81, 0x02,                         //    INPUT (Data,Var,Abs)
            (byte) 0x95, 0x05,                          //   REPORT_COUNT (5)
            (byte) 0x81, 0x03,                         //    INPUT (Cnst,Var,Abs)
            0x05, 0x0d,                         //    USAGE_PAGE (Digitizer)
            (byte) 0x85, 1,            //   REPORT_ID (Feature)
            0x09, 0x55,                         //    USAGE (Contact Count Maximum)
            0x09, 0x59,                         //    USAGE (Pad TYpe)
            0x75, 0x04,                         //    REPORT_SIZE (4)
            (byte) 0x95, 0x02,                         //    REPORT_COUNT (2)
            0x25, 0x0f,                         //    LOGICAL_MAXIMUM (15)
            (byte) 0xb1, 0x02,                         //    FEATURE (Data,Var,Abs)
            0x06, 0x00,(byte)  0xff,                   //    USAGE_PAGE (Vendor Defined)
            (byte) 0x85, 3,               //    REPORT_ID (PTPHQA)
            0x09, (byte) 0xC5,                         //    USAGE (Vendor Usage 0xC5)
            0x15, 0x00,                         //    LOGICAL_MINIMUM (0)
            0x26, (byte) 0xff, 0x00,                   //    LOGICAL_MAXIMUM (0xff)
            0x75, 0x08,                         //    REPORT_SIZE (8)
            (byte) 0x96, 0x00, 0x01,                   //    REPORT_COUNT (0x100 (256))
            (byte) 0xb1, 0x02,                         //    FEATURE (Data,Var,Abs)
            (byte) 0xc0,                               // END_COLLECTION
    };
}
