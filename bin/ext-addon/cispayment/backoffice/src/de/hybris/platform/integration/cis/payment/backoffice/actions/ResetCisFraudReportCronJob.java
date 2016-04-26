package de.hybris.platform.integration.cis.payment.backoffice.actions;


import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.integration.cis.payment.model.CisFraudReportCronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.zkoss.zul.Messagebox;

import javax.annotation.Resource;

public class ResetCisFraudReportCronJob implements CockpitAction<Object, Object> {


    @Resource
    private ModelService modelService;

    @Override
    public ActionResult<Object> perform(ActionContext<Object> actionContext) {
        final Object data = actionContext.getData();
        if (data instanceof CisFraudReportCronJobModel) {

            final CisFraudReportCronJobModel job = (CisFraudReportCronJobModel) data;

            job.setLastFraudReportEndTime(null);
            modelService.save(job);

            NotificationUtils.notifyUser(actionContext.getLabel("action.resetfraudreportendtimeaction.success"),
                    NotificationEvent.Type.SUCCESS, NotificationEvent.Behavior.TIMED);
            return new ActionResult<Object>(ActionResult.SUCCESS, job);
        }

        Messagebox.show(data + " (" + ActionResult.ERROR + ")");
        return new ActionResult<Object>(ActionResult.ERROR);

    }

    @Override
    public boolean canPerform(ActionContext<Object> actionContext) {
        final Object data = actionContext.getData();
        return data instanceof CisFraudReportCronJobModel;
    }

    @Override
    public boolean needsConfirmation(ActionContext<Object> actionContext) {
        return true;
    }

    @Override
    public String getConfirmationMessage(ActionContext<Object> actionContext) {
        return actionContext.getLabel("action.resetfraudreportendtimeaction.confirm");
    }
}