package api.source.svc.open.api.res.api;

import api.source.svc.core.AppSettings;
import api.source.svc.core.svc.apimon.SvcException;
import api.source.svc.nds.svc.NDSGroupSvc;
import api.source.svc.nds.svc.NDSUISvc;

@SubscriptionPlan(PlanTypeSet.NDS)
@PaidInFull
@Path("/api/api/v1.0/groups/{groupId}/accessList")
@Singleton
public class ApiNetworkAccessListResource extends ApiBaseResource {

  private final NDSUISvc _ndsUISvc;
  private final NDSGroupSvc _ndsGroupSvc;

  @Inject
  public ApiNetworkAccessListResource(
      final AppSettings pSettings, final NDSUISvc pNDSUISvc, final NDSGroupSvc pNDSGroupSvc) {
    super(pSettings);
    _ndsUISvc = pNDSUISvc;
    _ndsGroupSvc = pNDSGroupSvc;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @RolesAllowed({
    RoleSet.NAME.GROUP_READ_ONLY,
    RoleSet.NAME.GROUP_CHARTS_ADMIN,
    RoleSet.NAME.GLOBAL_STITCH_INTERNAL_ADMIN
  })
  public Response getWhitelist(
      @Context final HttpServletRequest pRequest,
      @Context final Group pGroup,
      @QueryParam("envelope") final Boolean pEnvelope)
      throws SvcException {}

  @GET
  @Path("/{entryValue}")
  @Produces({MediaType.APPLICATION_JSON})
  @RolesAllowed({RoleSet.NAME.GROUP_READ_ONLY, RoleSet.NAME.GLOBAL_STITCH_INTERNAL_ADMIN})
  public Response getNetworkPermissionEntry(
      @Context final Group pGroup,
      @PathParam("entryValue") final String pNetworkPermissionEntryValue,
      @QueryParam("envelope") final Boolean pEnvelope)
      throws SvcException {}

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
  @RolesAllowed({
    RoleSet.NAME.GROUP_ATLAS_ADMIN,
    RoleSet.NAME.GROUP_CHARTS_ADMIN,
    RoleSet.NAME.GLOBAL_STITCH_INTERNAL_ADMIN
  })
  @AllowTemporaryApiKeys
  public Response addAtlasWhitelist(
      @Context final HttpServletRequest pRequest,
      @Context final Group pGroup,
      @Context final AuditInfo pAuditInfo,
      @QueryParam("envelope") final Boolean pEnvelope,
      final List<ApiAtlasNetworkPermissionEntryView> pWhitelist)
      throws SvcException {}

  @DELETE
  @Path("/{entryValue}")
  @Produces({MediaType.APPLICATION_JSON})
  @RolesAllowed({RoleSet.NAME.GROUP_ATLAS_ADMIN, RoleSet.NAME.GLOBAL_STITCH_INTERNAL_ADMIN})
  public Response deleteWhitelist(
      @Context final HttpServletRequest pRequest,
      @Context final AuditInfo pAuditInfo,
      @PathParam("entryValue") final String pNetworkPermissionEntryValue,
      @QueryParam("envelope") final Boolean pEnvelope,
      @Context final Group pGroup)
      throws SvcException {}
}
