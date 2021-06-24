package io.github.kosmx.emotes.common;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SerializableConfig {
    public final ArrayList<ConfigEntry<?>> basics = new ArrayList<>();
    public final ArrayList<ConfigEntry<?>> expert = new ArrayList<>();
    public final ArrayList<ConfigEntry<?>> hidden = new ArrayList<>();

    /**
     * changelog
     * 2 - PlayerSafetyOption
     * 3 - Kale Ko changed a build-in emote, EmoteFixer to keep bound
     */
    public final static int staticConfigVersion = 3;


    public int configVersion; //this has a different job... not a config

    public final BooleanConfigEntry showDebug = new BooleanConfigEntry("debug", "showDebug", true, false, hidden);
    public final BooleanConfigEntry validateEmote = new BooleanConfigEntry("validate", false, true, hidden);

    public final FloatConfigEntry<Float> validThreshold = new FloatConfigEntry<>("validationThreshold", "validThreshold", 8f, true, hidden, "options.generic_value", 0.2f, 16f, 0f);


    public int[] fastMenuHash = new int[8];

    public void iterate(Consumer<ConfigEntry<?>> consumer){
        basics.forEach(consumer);
        expert.forEach(consumer);
    }

    public void iterateGeneral(Consumer<ConfigEntry<?>> consumer){
        basics.forEach(consumer);
    }

    public void iterateExpert(Consumer<ConfigEntry<?>> consumer){
        expert.forEach(consumer);
    }

    public static abstract class ConfigEntry<T>{
        final String name, oldConfig; //oldconfig for the old config name
        T value;
        final T defaultValue;
        final public boolean hasTooltip;
        final boolean isHidden;

        public ConfigEntry(String name, String oldconfig, T defVal, boolean hasTooltip, List<ConfigEntry<?>> collection, boolean hidden){
            this.name = name;
            this.oldConfig = oldconfig;
            this.hasTooltip = hasTooltip;
            defaultValue = defVal;
            value = defVal;
            collection.add(this);
            isHidden = hidden;
        }

        public ConfigEntry(String name, String oldconfig, T defVal, boolean hasTooltip, List<ConfigEntry<?>> collection){
            this(name, oldconfig, defVal, hasTooltip, collection, false);
        }
        public ConfigEntry(String name, T defVal, boolean hasTooltip, List<ConfigEntry<?>> collection){
            this(name, null, defVal, hasTooltip, collection);
        }
        public ConfigEntry(String name, T defVal, List<ConfigEntry<?>> collection, boolean hidden){
            this(name, null, defVal, false, collection, hidden);
        }

        public T get(){
            return value;
        }

        public void set(T newValue){
            this.value = newValue;
        }

        public String getName(){
            return name;
        }
        public String getOldConfigName(){
            return oldConfig;
        }

        public void resetToDefault(){
            this.value = this.defaultValue;
        }

        public boolean showEntry(){
            return !isHidden;
        }

    }

    public static class BooleanConfigEntry extends ConfigEntry<Boolean>{

        public BooleanConfigEntry(String name, String oldconfig, Boolean defVal, boolean hasTooltip, List<ConfigEntry<?>> collection, boolean hidden) {
            super(name, oldconfig, defVal, hasTooltip, collection, hidden);
        }
        public BooleanConfigEntry(String name, String oldconfig, Boolean defVal, boolean hasTooltip, List<ConfigEntry<?>> collection) {
            super(name, oldconfig, defVal, hasTooltip, collection);
        }

        public BooleanConfigEntry(String name, Boolean defVal, boolean hasTooltip, List<ConfigEntry<?>> collection) {
            super(name, defVal, hasTooltip, collection);
        }
        public BooleanConfigEntry(String name, Boolean defVal, List<ConfigEntry<?>> collection, boolean hidden) {
            super(name, defVal, collection, hidden);
        }
    }

    public static class FloatConfigEntry<T extends Number> extends ConfigEntry<Float> {
        final private String formatKey;
        public final float min, max, step;
        public FloatConfigEntry(String name, String oldconfig, Float defVal, boolean hasTooltip, List<ConfigEntry<?>> collection, String formatKey, float min, float max, float step) {
            super(name, oldconfig, defVal, hasTooltip, collection);
            this.formatKey = formatKey;
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public FloatConfigEntry(String name, Float defVal, boolean hasTooltip, List<ConfigEntry<?>> collection, String formatKey, float min, float step, float max) {
            this(name, null, defVal, hasTooltip, collection, formatKey, min, max, step);

        }

        public String getFormatKey() {
            return formatKey;
        }

        public double getConfigVal(){
            return Math.sqrt(this.get());
        }
        public void setConfigVal(double newVal){
            this.set((float) Math.pow(newVal, 2));
        }
        public T getTextVal(){
            return (T) get();
        }
    }
}
