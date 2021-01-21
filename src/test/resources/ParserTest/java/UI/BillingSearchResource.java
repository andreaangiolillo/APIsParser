package open.source.api.test.res;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import open.source.svc.api.model.RoleSet;
import open.source.svc.api.res.util.BaseResource;
import open.source.svc.api.res.util.ResourceCtx;
import open.source.svc.api.res.util.UiCall;
import open.source.svc.api.res.util.UiCall.GroupSource;
import open.source.svc.api.svc.billing.InvoiceSvc;
import org.bson.types.ObjectId;

@Path("/a/b/search")
@Singleton
public class BillingSearchResource extends BaseResource {

  final InvoiceSvc _invoiceSvc;

  @Inject
  public BillingSearchResource(final ResourceCtx pResourceCtx, final InvoiceSvc pInvoiceSvc) {
    super(pResourceCtx);
    _invoiceSvc = pInvoiceSvc;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @UiCall(roles = RoleSet.GLOBAL_READ_ONLY, groupSource = GroupSource.NONE)
  public Response searchPageOpenSource(@Context final HttpServletRequest pRequest) {}

  @GET
  @Path("/invoices/{invoiceId}")
  @Produces(MediaType.APPLICATION_JSON)
  @UiCall(roles = RoleSet.GLOBAL_READ_ONLY, groupSource = GroupSource.NONE)
  public Response getInvoiceByInvoiceIdOpenSource(
      @PathParam("invoiceId") final ObjectId pInvoiceId) {}

  @GET
  @Path("/payments/{paymentId}")
  @Produces(MediaType.APPLICATION_JSON)
  @UiCall(roles = RoleSet.GLOBAL_READ_ONLY, groupSource = GroupSource.NONE)
  public Response getInvoiceByPaymentIdOpenSource(
      @PathParam("paymentId") final ObjectId pPaymentId) {}

  @GET
  @Path("/charges/{chargeIdOrPaymentIntentId}")
  @Produces(MediaType.APPLICATION_JSON)
  @UiCall(roles = RoleSet.GLOBAL_READ_ONLY, groupSource = GroupSource.NONE)
  public Response getInvoiceByStripeChargeIdOrPaymentIntentIdOpenSource(
      @PathParam("chargeIdOrPaymentIntentId") final String pChargeIdOrPaymentIntentId) {}

  protected InvoiceSvc getInvoiceSvc() {
    return _invoiceSvc;
  }
}
