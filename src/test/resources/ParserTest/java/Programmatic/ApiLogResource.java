package api.source.svc.api.api.res.api;

import api.source.svc.api.api.res.ApiBaseResource;
import api.source.svc.api.api.view.api.ApiAtlasAuditLogView;
import api.source.svc.api.model.AppUser;
import api.source.svc.api.model.Group;
import api.source.svc.api.model.RoleSet;
import api.source.svc.api.model.billing.PlanTypeSet;
import api.source.svc.api.model.event.info.AuditInfo;
import api.source.svc.api.res.filter.PaidInFull;
import api.source.svc.api.res.filter.SubscriptionPlan;
import api.source.svc.core.AppSettings;
import api.source.svc.core.res.apimon.PATCH;
import api.source.svc.nds.svc.NDSUISvc;

@SubscriptionPlan(PlanTypeSet.NDS)
@PaidInFull
@Path("/api/api/v1.0/groups/{resource}/auditLog")
@Singleton
public class ApiLogResource extends ApiBaseResource {

  private final NDSUISvc _ndsUISvc;

  @Inject
  public ApiLogResource(final AppSettings pSettings, final NDSUISvc pNDSUISvc) {
    super(pSettings);
    _ndsUISvc = pNDSUISvc;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed({RoleSet.NAME.GROUP_ATLAS_ADMIN})
  public Response getAuditLog(
      @Context final Group pGroup, @QueryParam("envelope") final Boolean pEnvelope) {}

  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed({RoleSet.NAME.GROUP_ATLAS_ADMIN})
  public Response patchAuditLog(
      @Context final Group pGroup,
      @Context final AppUser pUser,
      @Context final AuditInfo pAuditInfo,
      @QueryParam("envelope") final Boolean pEnvelope,
      final ApiAtlasAuditLogView pAuditLogView) {}
}
