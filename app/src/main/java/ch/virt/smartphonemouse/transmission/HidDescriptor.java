package ch.virt.smartphonemouse.transmission;

public class HidDescriptor {

    // Tag IDs
    private static final byte TAG_USAGE_PAGE = 0x05;
    private static final byte TAG_USAGE = 0x09;
    private static final byte TAG_COLLECTION = (byte) 0xA1;
    private static final byte TAG_USAGE_MIN = 0x19;
    private static final byte TAG_USAGE_MAX = 0x29;
    private static final byte TAG_LOGICAL_MIN = 0x15;
    private static final byte TAG_LOGICAL_MAX = 0x25;
    private static final byte TAG_REPORT_ID = (byte) 0x85;
    private static final byte TAG_REPORT_COUNT = (byte) 0x95;
    private static final byte TAG_REPORT_SIZE = 0x75;
    private static final byte TAG_INPUT = (byte) 0x81;
    private static final byte TAG_END_COLLECTION = (byte) 0xC0;

    // Usage Page IDs
    private static final byte UP_GENERIC_DESKTOP = 0x01;
    private static final byte UP_BUTTON = 0x09;

    // Usage IDs
    private static final byte U_MOUSE = 0x02;
    private static final byte U_POINTER = 0x01;
    private static final byte U_X = 0x30;
    private static final byte U_Y = 0x31;
    private static final byte U_WHEEL = 0x38;

    // Collection IDs
    private static final byte C_PHYSICAL = 0x00;
    private static final byte C_APPLICATION = 0x01;

    // Input IDs
    private static final byte I_DAT_VAR_ABS = 0x02;
    private static final byte I_CON = 0x01;
    private static final byte I_DAT_VAR_REL = 0x06;

    // Descriptor
    public static final byte[] DESCRIPTOR = new byte[] {
            TAG_USAGE_PAGE,   UP_GENERIC_DESKTOP,
            TAG_USAGE,        U_MOUSE,
            TAG_COLLECTION,   C_APPLICATION,

                TAG_REPORT_ID,    1,                    // Record Id
                TAG_USAGE,        U_POINTER,
                TAG_COLLECTION,   C_PHYSICAL,

                    TAG_USAGE_PAGE,   UP_BUTTON,
                    TAG_USAGE_MIN,    1,                    // First Button 1
                    TAG_USAGE_MAX,    3,                    // Last Button 3
                    TAG_LOGICAL_MIN,  0,
                    TAG_LOGICAL_MAX,  1,
                    TAG_REPORT_SIZE,  1,
                    TAG_REPORT_COUNT, 3,
                    TAG_INPUT,        I_DAT_VAR_ABS,

                    TAG_REPORT_SIZE,  5,
                    TAG_REPORT_COUNT, 1,
                    TAG_INPUT,        I_CON,

                    TAG_USAGE_PAGE,   UP_GENERIC_DESKTOP,
                    TAG_USAGE,        U_X,
                    TAG_USAGE,        U_Y,
                    TAG_USAGE,        U_WHEEL,
                    TAG_LOGICAL_MIN,  (byte) 0x81,          // -127
                    TAG_LOGICAL_MAX,  0x7F,                 //  127
                    TAG_REPORT_SIZE,  8,
                    TAG_REPORT_COUNT, 3,
                    TAG_INPUT,        I_DAT_VAR_REL,

                TAG_END_COLLECTION,

            TAG_END_COLLECTION
    };


}
