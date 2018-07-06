package org.apereo.portal.fbms.data.filter;

import org.apereo.portal.fbms.data.ExtensionFilter;
import org.apereo.portal.fbms.data.ExtensionFilterChain;
import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FormEntity;
import org.apereo.portal.fbms.data.SubmissionEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides support for the FBMS <em>Form Forwarding</em> feature, through which FBMS can send the
 * user to a second (or subsequent) form when he or she completes a form.
 */
@Component
public class FormForwardingExtensionFilter extends AbstractExtensionFilter<SubmissionEntity> {

    private static final String FORM_FORWARD_SESSION_ATTRIBUTE = FormForwardingExtensionFilter.class.getName() + ".formForward";

    private static final String FORM_FORWARD_HEADER_NAME = "X-FBMS-FormForward";

    public FormForwardingExtensionFilter() {
        super(ExtensionFilter.ORDER_FIRST); // First-in, last-out
    }

    @Override
    public boolean appliesTo(FbmsEntity entity, HttpServletRequest request) {
        // Applies only when a Submission is posted
        return SubmissionEntity.class.isInstance(entity)
                && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * Use this method (within another, custom {@link ExtensionFilter}) to invoke the form-forwarding feature.
     */
    public void forward(HttpServletRequest request, FormEntity form) {

        if (!appliesTo(form, request)) {
            throw new IllegalStateException("Form-forwarding does not apply to this request");
        }
        if (form == null) {
            throw new IllegalArgumentException("Argument 'form' cannot be null");
        }

        request.setAttribute(FORM_FORWARD_SESSION_ATTRIBUTE, form);

    }

    @Override
    public SubmissionEntity doFilter(SubmissionEntity entity, HttpServletRequest request, HttpServletResponse response, ExtensionFilterChain<SubmissionEntity> chain) {

        final SubmissionEntity rslt = chain.doFilter(entity);

        final FormEntity form = (FormEntity) request.getAttribute(FORM_FORWARD_SESSION_ATTRIBUTE);
        if (form != null) {
            // Send the user on to the specified form...
            response.setHeader(FORM_FORWARD_HEADER_NAME, form.getId().getFname());
        }

        return rslt;

    }

}
