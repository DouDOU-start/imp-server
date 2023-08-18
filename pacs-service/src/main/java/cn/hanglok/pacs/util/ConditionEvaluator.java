package cn.hanglok.pacs.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className ConditionEvaluatorUtils
 * @description 组合条件校验
 * @date 2023/6/14 10:22
 */
public class ConditionEvaluator {

    /**
     * 组件接口，定义组合对象和叶子对象共同行为
     */
    interface Component {
        boolean satisfiesCondition();
    }

    public static class Leaf implements Component {
        private final boolean condition;

        public Leaf(boolean condition) {
            this.condition = condition;
        }

        @Override
        public boolean satisfiesCondition() {
            return condition;
        }
    }

    /**
     * 组合对象，可以包含子组件，并对子组件进行操作
     */
    public static class Composite implements Component {

        private final List<Component> components;

        public Composite() {
            this.components = new ArrayList<>();
        }

        public void addComponent(Component component) {
            components.add(component);
        }

        public void removeComponent(Component component) {
            components.remove(component);
        }

        @Override
        public boolean satisfiesCondition() {
            for (Component component : components) {
                if (! component.satisfiesCondition()) {
                    return false;
                }
            }
            return true;
        }
    }

}
