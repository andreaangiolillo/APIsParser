package api.open.svc.source.api.res.api;

import api.open.svc.atm.svc.AutomationAgentAuditSvc;
import api.open.svc.core.AppSettings;
import api.open.svc.nds.svc.NDSLookupSvc;
import api.open.svc.source.api.res.ApiBasePerformanceAdvisorResource;
import api.open.svc.source.model.AppUser;
import api.open.svc.source.model.FeatureFlag;
import api.open.svc.source.model.Group;
import api.open.svc.source.model.Organization;
import api.open.svc.source.model.RoleSet;
import api.open.svc.source.model.billing.PlanTypeSet;
import api.open.svc.source.model.event.AccessEvent;
import api.open.svc.source.res.filter.AccessAudit;
import api.open.svc.source.res.filter.Feature;
import api.open.svc.source.res.filter.PaidInFull;
import api.open.svc.source.res.filter.RateLimiter;
import api.open.svc.source.res.filter.SubscriptionPlan;
import api.open.svc.source.svc.SlowQueryLogSvc;
import api.open.svc.source.svc.explorer.DataExplorerSvc;
import api.open.svc.source.svc.host.HostSvc;
import api.open.svc.source.svc.performanceadvisor.PerformanceAdvisorSvc;
import org.bson.types.ObjectId;

@SubscriptionPlan(PlanTypeSet.PREMIUM_ONPREM_NDS)
@PaidInFull
@Path("/api/api/v1.0/groups/{groupId}/processes/{processId}/performanceAdvisor")
@Singleton
public class ApiPerformanceAdvisorResource extends ApiBasePerformanceAdvisorResource {

  @Inject
  public ApiPerformanceAdvisorResource(
      final AppSettings pSettings,
      final HostSvc pHostSvc,
      final SlowQueryLogSvc pSlowQueryLogSvc,
      final PerformanceAdvisorSvc pPerformanceAdvisorSvc,
      final AutomationAgentAuditSvc pAutomationAgentAuditSvc,
      final DataExplorerSvc pDataExplorerSvc,
      final NDSLookupSvc pNdsLookupSvc) {
    super(
        pSettings,
        pHostSvc,
        pSlowQueryLogSvc,
        pPerformanceAdvisorSvc,
        pAutomationAgentAuditSvc,
        pDataExplorerSvc,
        pNdsLookupSvc);
  }

  @GET
  @Path("/namespaces")
  @Produces({MediaType.APPLICATION_JSON})
  @Feature(FeatureFlag.PERFORMANCE_ADVISOR)
  @RolesAllowed({RoleSet.NAME.GROUP_READ_ONLY})
  @RateLimiter(RateLimiter.Feature.PERFORMANCE_ADVISOR)
  @AccessAudit(
      auditableRoles = RoleSet.PII_AUDITABLE,
      auditEventType = AccessEvent.Type.PERFORMANCE_ADVISOR,
      hostSource = AccessAudit.HostSource.PATH)
  public Response getNamespaces(
      @Context final HttpServletRequest pRequest,
      @Context final Organization pOrganization,
      @Context final Group pGroup,
      @Context final AppUser pCurrentUser,
      @PathParam("groupId") final ObjectId pGroupId,
      @PathParam("processId") final String pProcessId,
      @QueryParam("since") final Long pSince,
      @QueryParam("duration") final Long pDuration,
      @QueryParam("envelope") @DefaultValue("false") final Boolean pEnvelope,
      @QueryParam("improvementMin") @DefaultValue("" + PerformanceAdvisorSvc.IMPROVEMENT_MINIMUM)
          final int pImprovementMinimum)
      throws Exception {}

  @GET
  @Path("/suggestedIndexes")
  @Produces({MediaType.APPLICATION_JSON})
  @Feature(FeatureFlag.PERFORMANCE_ADVISOR)
  @RolesAllowed({RoleSet.NAME.GROUP_READ_ONLY})
  @RateLimiter(RateLimiter.Feature.PERFORMANCE_ADVISOR)
  @AccessAudit(
      auditableRoles = RoleSet.PII_AUDITABLE,
      auditEventType = AccessEvent.Type.PERFORMANCE_ADVISOR,
      hostSource = AccessAudit.HostSource.PATH)
  public Response getSuggestedIndexes(
      @Context final HttpServletRequest pRequest,
      @Context final Organization pOrganization,
      @Context final Group pGroup,
      @Context final AppUser pCurrentUser,
      @PathParam("groupId") final ObjectId pGroupId,
      @PathParam("processId") final String pProcessId,
      @QueryParam("namespaces") final List<String> pNamespaces,
      @QueryParam("since") final Long pSince,
      @QueryParam("duration") final Long pDuration,
      @QueryParam("nIndexes") final Long pNIndexes,
      @QueryParam("nExamples") @DefaultValue("5") final int pNExamples,
      @QueryParam("envelope") @DefaultValue("false") final Boolean pEnvelope,
      @QueryParam("improvementMin") @DefaultValue("" + PerformanceAdvisorSvc.IMPROVEMENT_MINIMUM)
          final int pImprovementMinimum)
      throws Exception {}

  @GET
  @Path("/slowQueryLogs")
  @Produces({MediaType.APPLICATION_JSON})
  @Feature(FeatureFlag.PERFORMANCE_ADVISOR)
  @RolesAllowed({RoleSet.NAME.GROUP_DATA_ACCESS_ANY})
  @RateLimiter(RateLimiter.Feature.PERFORMANCE_ADVISOR)
  public Response getSlowQueryLogs(
      @Context final HttpServletRequest pRequest,
      @Context final Organization pOrganization,
      @Context final Group pGroup,
      @Context final AppUser pCurrentUser,
      @PathParam("groupId") final ObjectId pGroupId,
      @PathParam("processId") final String pProcessId,
      @QueryParam("namespaces") final List<String> pNamespaces,
      @QueryParam("since") final Long pSince,
      @QueryParam("duration") final Long pDuration,
      @QueryParam("nLogs") @DefaultValue("20000") final Long pNLogs,
      @QueryParam("envelope") @DefaultValue("false") final Boolean pEnvelope)
      throws Exception {}
}
