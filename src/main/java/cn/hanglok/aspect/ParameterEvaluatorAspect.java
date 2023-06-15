package cn.hanglok.aspect;

import cn.hanglok.exception.ResponseException;
import cn.hanglok.util.ConditionEvaluator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Allen
 * @version 1.0
 * @className ParameterAspect
 * @description TODO
 * @date 2023/6/13 17:53
 */
@Aspect
@Component
public class ParameterEvaluatorAspect {

    @Before(value = "execution(* cn.hanglok.controller.DicomController.getSimpleDicomList(String, int, int)) && args(patientSex, currentPage, pageSize)",
            argNames = "patientSex, currentPage, pageSize")
    public void getSimpleDicomList(String patientSex, int currentPage, int pageSize) {

        // 创建组合校验
        ConditionEvaluator.Composite composite = new ConditionEvaluator.Composite() {{

            // 性别校验
            addComponent(new ConditionEvaluator.Leaf(patientSex == null ||
                    Arrays.asList(new String[]{"F", "M"}).contains(patientSex)));

            // 页码校验
            addComponent(new ConditionEvaluator.Leaf(currentPage > 0 && pageSize > 0 ));
        }};

        if (! composite.satisfiesCondition()) {
            throw new ResponseException.UnprocessableEntityException();
        }
    }


}
