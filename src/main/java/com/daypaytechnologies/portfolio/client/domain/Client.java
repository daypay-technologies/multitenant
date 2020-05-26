package com.daypaytechnologies.portfolio.client.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.daypaytechnologies.core.api.JsonCommand;
import com.daypaytechnologies.core.data.ApiParameterError;
import com.daypaytechnologies.core.data.DataValidatorBuilder;
import com.daypaytechnologies.core.domain.AbstractPersistableCustom;
import com.daypaytechnologies.core.domain.ClientEnumerations;
import com.daypaytechnologies.core.domain.ClientStatus;
import com.daypaytechnologies.core.domain.LegalForm;
import com.daypaytechnologies.core.exception.PlatformApiDataValidationException;
import com.daypaytechnologies.core.service.DateUtils;
import com.daypaytechnologies.portfolio.client.api.ClientApiConstants;
import com.daypaytechnologies.security.service.RandomPasswordGenerator;
import com.daypaytechnologies.user.domain.AppUser;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Table(name = "m_client", uniqueConstraints = { @UniqueConstraint(columnNames = { "account_no" }, name = "account_no_UNIQUE"), //
        @UniqueConstraint(columnNames = { "mobile_no" }, name = "mobile_no_UNIQUE") })
public final class Client extends AbstractPersistableCustom<Long> {

    @Column(name = "account_no", length = 20, unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "status_enum", nullable = false)
    private Integer status;
    
    @Column(name = "activation_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date activationDate;

    @Column(name = "office_joining_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date officeJoiningDate;

    @Column(name = "firstname", length = 50, nullable = true)
    private String firstname;

    @Column(name = "middlename", length = 50, nullable = true)
    private String middlename;

    @Column(name = "lastname", length = 50, nullable = true)
    private String lastname;

    @Column(name = "fullname", length = 100, nullable = true)
    private String fullname;

    @Column(name = "display_name", length = 100, nullable = false)
    private String displayName;

    @Column(name = "mobile_no", length = 50, nullable = false, unique = true)
    private String mobileNo;
	
	public String getMobileNo() {
		return mobileNo;
	}

	@Column(name = "email_address", length = 50, unique = true)
    private String emailAddress;

	@Column(name = "is_staff", nullable = false)
    private boolean isStaff;

    @Column(name = "external_id", length = 100, nullable = true, unique = true)
    private String externalId;

    @Column(name = "date_of_birth", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Transient
    private boolean accountNumberRequiresAutoGeneration = false;


    @Column(name = "closedon_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date closureDate;

    @Column(name = "rejectedon_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date rejectionDate;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "rejectedon_userid", nullable = true)
    private AppUser rejectedBy;

    @Column(name = "withdrawn_on_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date withdrawalDate;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "withdraw_on_userid", nullable = true)
    private AppUser withdrawnBy;

    @Column(name = "reactivated_on_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date reactivateDate;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "reactivated_on_userid", nullable = true)
    private AppUser reactivatedBy;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "closedon_userid", nullable = true)
    private AppUser closedBy;

    @Column(name = "submittedon_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date submittedOnDate;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "submittedon_userid", nullable = true)
    private AppUser submittedBy;

    @Column(name = "updated_on", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date updatedOnDate;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true)
    private AppUser updatedBy;

    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "activatedon_userid", nullable = true)
    private AppUser activatedBy;

    @Column(name = "default_savings_product", nullable = true)
    private Long savingsProductId;
    
    @Column(name = "default_savings_account", nullable = true)
    private Long savingsAccountId;
    
    @Column(name = "legal_form_enum", nullable = true)
    private Integer legalForm;

    @Column(name = "reopened_on_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date reopenedDate;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "reopened_by_userid", nullable = true)
    private AppUser reopenedBy;

    public static Client createNew(final AppUser currentUser,
            final Long savingsProductId, final Integer legalForm, final JsonCommand command) {

        final String accountNo = command.stringValueOfParameterNamed(ClientApiConstants.accountNoParamName);
        final String externalId = command.stringValueOfParameterNamed(ClientApiConstants.externalIdParamName);
        final String mobileNo = command.stringValueOfParameterNamed(ClientApiConstants.mobileNoParamName);
		final String emailAddress = command.stringValueOfParameterNamed(ClientApiConstants.emailAddressParamName);

        final String firstname = command.stringValueOfParameterNamed(ClientApiConstants.firstnameParamName);
        final String middlename = command.stringValueOfParameterNamed(ClientApiConstants.middlenameParamName);
        final String lastname = command.stringValueOfParameterNamed(ClientApiConstants.lastnameParamName);
        final String fullname = command.stringValueOfParameterNamed(ClientApiConstants.fullnameParamName);
		
		final boolean isStaff = command.booleanPrimitiveValueOfParameterNamed(ClientApiConstants.isStaffParamName);

        final LocalDate dataOfBirth = command.localDateValueOfParameterNamed(ClientApiConstants.dateOfBirthParamName);

        ClientStatus status = ClientStatus.PENDING;
        boolean active = false;
        if (command.hasParameter("active")) {
            active = command.booleanPrimitiveValueOfParameterNamed(ClientApiConstants.activeParamName);
        }

        LocalDate activationDate = null;
        LocalDate officeJoiningDate = null;
        if (active) {
            status = ClientStatus.ACTIVE;
            activationDate = command.localDateValueOfParameterNamed(ClientApiConstants.activationDateParamName);
            officeJoiningDate = activationDate;
        }

        LocalDate submittedOnDate = new LocalDate();
        if (active && submittedOnDate.isAfter(activationDate)) {
            submittedOnDate = activationDate;
        }
        if (command.hasParameter(ClientApiConstants.submittedOnDateParamName)) {
            submittedOnDate = command.localDateValueOfParameterNamed(ClientApiConstants.submittedOnDateParamName);
        }
        final Long savingsAccountId = null;
        return new Client(currentUser, status, accountNo, firstname, middlename, lastname, fullname,
                activationDate, officeJoiningDate, externalId, mobileNo, emailAddress, submittedOnDate, savingsProductId, savingsAccountId, dataOfBirth, legalForm, isStaff);
    }

    protected Client() {
    	this.setLegalForm(null);
    }

    private Client(final AppUser currentUser, final ClientStatus status,
            final String accountNo, final String firstname, final String middlename, final String lastname, final String fullname,
            final LocalDate activationDate, final LocalDate officeJoiningDate, final String externalId, final String mobileNo, final String emailAddress,
            final LocalDate submittedOnDate, final Long savingsProductId, final Long savingsAccountId,
            final LocalDate dateOfBirth, final Integer legalForm, final Boolean isStaff) {

        if (StringUtils.isBlank(accountNo)) {
            this.accountNumber = new RandomPasswordGenerator(19).generate();
            this.accountNumberRequiresAutoGeneration = true;
        } else {
            this.accountNumber = accountNo;
        }

        this.submittedOnDate = submittedOnDate.toDate();
        this.submittedBy = currentUser;

        this.status = status.getValue();
        if (StringUtils.isNotBlank(externalId)) {
            this.externalId = externalId.trim();
        } else {
            this.externalId = null;
        }

        if (StringUtils.isNotBlank(mobileNo)) {
            this.mobileNo = mobileNo.trim();
        } else {
            this.mobileNo = null;
        }

		if (StringUtils.isNotBlank(emailAddress)) {
            this.emailAddress = emailAddress.trim();
        } else {
            this.emailAddress = null;
        }

        if (activationDate != null) {
            this.activationDate = activationDate.toDateTimeAtStartOfDay().toDate();
            this.activatedBy = currentUser;
        }
        if (officeJoiningDate != null) {
            this.officeJoiningDate = officeJoiningDate.toDateTimeAtStartOfDay().toDate();
        }
        if (StringUtils.isNotBlank(firstname)) {
            this.firstname = firstname.trim();
        } else {
            this.firstname = null;
        }

        if (StringUtils.isNotBlank(middlename)) {
            this.middlename = middlename.trim();
        } else {
            this.middlename = null;
        }

        if (StringUtils.isNotBlank(lastname)) {
            this.lastname = lastname.trim();
        } else {
            this.lastname = null;
        }

        if (StringUtils.isNotBlank(fullname)) {
            this.fullname = fullname.trim();
        } else {
            this.fullname = null;
        }

        this.savingsProductId = savingsProductId;
        this.savingsAccountId = savingsAccountId;
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth.toDateTimeAtStartOfDay().toDate();
        }
        this.setLegalForm(legalForm);

        deriveDisplayName();
        validate();
    }

    private void validate() {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        validateNameParts(dataValidationErrors);
        validateActivationDate(dataValidationErrors);

        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }

    }
    
    private void validateUpdate() {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        //Not validating name parts while update request as firstname/lastname can be along with fullname 
        //when we change clientType from Individual to Organisation or vice-cersa
        validateActivationDate(dataValidationErrors);

        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }

    }

    public boolean isAccountNumberRequiresAutoGeneration() {
        return this.accountNumberRequiresAutoGeneration;
    }

    public void setAccountNumberRequiresAutoGeneration(final boolean accountNumberRequiresAutoGeneration) {
        this.accountNumberRequiresAutoGeneration = accountNumberRequiresAutoGeneration;
    }

    public boolean identifiedBy(final String identifier) {
        return identifier.equalsIgnoreCase(this.externalId);
    }

    public boolean identifiedBy(final Long clientId) {
        return getId().equals(clientId);
    }

    public void updateAccountNo(final String accountIdentifier) {
        this.accountNumber = accountIdentifier;
        this.accountNumberRequiresAutoGeneration = false;
    }

    public void activate(final AppUser currentUser, final DateTimeFormatter formatter, final LocalDate activationLocalDate) {

        if (isActive()) {
            final String defaultUserMessage = "Cannot activate client. Client is already active.";
            final ApiParameterError error = ApiParameterError.parameterError("error.msg.clients.already.active", defaultUserMessage,
                    ClientApiConstants.activationDateParamName, activationLocalDate.toString(formatter));

            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            dataValidationErrors.add(error);

            throw new PlatformApiDataValidationException(dataValidationErrors);
        }

        this.activationDate = activationLocalDate.toDate();
        this.activatedBy = currentUser;
        this.officeJoiningDate = this.activationDate;
        this.status = ClientStatus.ACTIVE.getValue();
        
        // in case a closed client is being re open
        this.closureDate = null;
        this.closedBy = null;

        validate();
    }

    public boolean isNotActive() {
        return !isActive();
    }

    public boolean isActive() {
        return ClientStatus.fromInt(this.status).isActive();
    }

    public boolean isClosed() {
        return ClientStatus.fromInt(this.status).isClosed();
    }

    public boolean isTransferInProgress() {
        return ClientStatus.fromInt(this.status).isTransferInProgress();
    }

    public boolean isTransferOnHold() {
        return ClientStatus.fromInt(this.status).isTransferOnHold();
    }

    public boolean isTransferInProgressOrOnHold() {
        return isTransferInProgress() || isTransferOnHold();
    }

    public boolean isNotPending() {
        return !isPending();
    }

    public boolean isPending() {
        return ClientStatus.fromInt(this.status).isPending();
    }

    private boolean isDateInTheFuture(final LocalDate localDate) {
        return localDate.isAfter(DateUtils.getLocalDateOfTenant());
    }
    
    public boolean isRejected() {
        return ClientStatus.fromInt(this.status).isRejected();
    }
    
    public boolean isWithdrawn() {
        return ClientStatus.fromInt(this.status).isWithdrawn();
    }
    
    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(9);

        if (command.isChangeInIntegerParameterNamed(ClientApiConstants.statusParamName, this.status)) {
            final Integer newValue = command.integerValueOfParameterNamed(ClientApiConstants.statusParamName);
            actualChanges.put(ClientApiConstants.statusParamName, ClientEnumerations.status(newValue));
            this.status = ClientStatus.fromInt(newValue).getValue();
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.accountNoParamName, this.accountNumber)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.accountNoParamName);
            actualChanges.put(ClientApiConstants.accountNoParamName, newValue);
            this.accountNumber = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.externalIdParamName, this.externalId)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.externalIdParamName);
            actualChanges.put(ClientApiConstants.externalIdParamName, newValue);
            this.externalId = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.mobileNoParamName, this.mobileNo)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.mobileNoParamName);
            actualChanges.put(ClientApiConstants.mobileNoParamName, newValue);
            this.mobileNo = StringUtils.defaultIfEmpty(newValue, null);
        }
		
		if (command.isChangeInStringParameterNamed(ClientApiConstants.emailAddressParamName, this.emailAddress)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.emailAddressParamName);
            actualChanges.put(ClientApiConstants.emailAddressParamName, newValue);
            this.emailAddress = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.firstnameParamName, this.firstname)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.firstnameParamName);
            actualChanges.put(ClientApiConstants.firstnameParamName, newValue);
            this.firstname = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.middlenameParamName, this.middlename)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.middlenameParamName);
            actualChanges.put(ClientApiConstants.middlenameParamName, newValue);
            this.middlename = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.lastnameParamName, this.lastname)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.lastnameParamName);
            actualChanges.put(ClientApiConstants.lastnameParamName, newValue);
            this.lastname = StringUtils.defaultIfEmpty(newValue, null);
        }

        if (command.isChangeInStringParameterNamed(ClientApiConstants.fullnameParamName, this.fullname)) {
            final String newValue = command.stringValueOfParameterNamed(ClientApiConstants.fullnameParamName);
            actualChanges.put(ClientApiConstants.fullnameParamName, newValue);
            this.fullname = newValue;
        }

        if (command.isChangeInLongParameterNamed(ClientApiConstants.savingsProductIdParamName, savingsProductId())) {
            final Long newValue = command.longValueOfParameterNamed(ClientApiConstants.savingsProductIdParamName);
            actualChanges.put(ClientApiConstants.savingsProductIdParamName, newValue);
        }
        
        if (command.isChangeInIntegerParameterNamed(ClientApiConstants.legalFormIdParamName, this.getLegalForm())) {
            final Integer newValue = command.integerValueOfParameterNamed(ClientApiConstants.legalFormIdParamName);
            if(newValue != null)
            {
            	LegalForm legalForm = LegalForm.fromInt(newValue);
            	if(legalForm != null)
            	{
            		actualChanges.put(ClientApiConstants.legalFormIdParamName, ClientEnumerations.legalForm(newValue));
                    this.setLegalForm(legalForm.getValue());
                    if(legalForm.isPerson()){
                        this.fullname = null;
                    }else if(legalForm.isEntity()){
                        this.firstname = null;
                        this.lastname = null;
                        this.displayName = null;
                    }
            	}
            	else
            	{
            		actualChanges.put(ClientApiConstants.legalFormIdParamName, null);
                    this.setLegalForm(null);
            	}
            }
            else
            {
            	actualChanges.put(ClientApiConstants.legalFormIdParamName, null);
                this.setLegalForm(null);
            }
        }

        final String dateFormatAsInput = command.dateFormat();
        final String localeAsInput = command.locale();

        if (command.isChangeInLocalDateParameterNamed(ClientApiConstants.activationDateParamName, getActivationLocalDate())) {
            final String valueAsInput = command.stringValueOfParameterNamed(ClientApiConstants.activationDateParamName);
            actualChanges.put(ClientApiConstants.activationDateParamName, valueAsInput);
            actualChanges.put(ClientApiConstants.dateFormatParamName, dateFormatAsInput);
            actualChanges.put(ClientApiConstants.localeParamName, localeAsInput);

            final LocalDate newValue = command.localDateValueOfParameterNamed(ClientApiConstants.activationDateParamName);
            this.activationDate = newValue.toDate();
            this.officeJoiningDate = this.activationDate;
        }

        if (command.isChangeInLocalDateParameterNamed(ClientApiConstants.dateOfBirthParamName, dateOfBirthLocalDate())) {
            final String valueAsInput = command.stringValueOfParameterNamed(ClientApiConstants.dateOfBirthParamName);
            actualChanges.put(ClientApiConstants.dateOfBirthParamName, valueAsInput);
            actualChanges.put(ClientApiConstants.dateFormatParamName, dateFormatAsInput);
            actualChanges.put(ClientApiConstants.localeParamName, localeAsInput);

            final LocalDate newValue = command.localDateValueOfParameterNamed(ClientApiConstants.dateOfBirthParamName);
            this.dateOfBirth = newValue.toDate();
        }

        if (command.isChangeInLocalDateParameterNamed(ClientApiConstants.submittedOnDateParamName, getSubmittedOnDate())) {
            final String valueAsInput = command.stringValueOfParameterNamed(ClientApiConstants.submittedOnDateParamName);
            actualChanges.put(ClientApiConstants.submittedOnDateParamName, valueAsInput);
            actualChanges.put(ClientApiConstants.dateFormatParamName, dateFormatAsInput);
            actualChanges.put(ClientApiConstants.localeParamName, localeAsInput);

            final LocalDate newValue = command.localDateValueOfParameterNamed(ClientApiConstants.submittedOnDateParamName);
            this.submittedOnDate = newValue.toDate();
        }

        validateUpdate();

        deriveDisplayName();

        return actualChanges;
    }

    private void validateNameParts(final List<ApiParameterError> dataValidationErrors) {
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("client");

        if (StringUtils.isNotBlank(this.fullname)) {

            baseDataValidator.reset().parameter(ClientApiConstants.firstnameParamName).value(this.firstname)
                    .mustBeBlankWhenParameterProvided(ClientApiConstants.fullnameParamName, this.fullname);

            baseDataValidator.reset().parameter(ClientApiConstants.middlenameParamName).value(this.middlename)
                    .mustBeBlankWhenParameterProvided(ClientApiConstants.fullnameParamName, this.fullname);

            baseDataValidator.reset().parameter(ClientApiConstants.lastnameParamName).value(this.lastname)
                    .mustBeBlankWhenParameterProvided(ClientApiConstants.fullnameParamName, this.fullname);
        } else {

            baseDataValidator.reset().parameter(ClientApiConstants.firstnameParamName).value(this.firstname).notBlank()
                    .notExceedingLengthOf(50);
            baseDataValidator.reset().parameter(ClientApiConstants.middlenameParamName).value(this.middlename).ignoreIfNull()
                    .notExceedingLengthOf(50);
            baseDataValidator.reset().parameter(ClientApiConstants.lastnameParamName).value(this.lastname).notBlank()
                    .notExceedingLengthOf(50);
        }
    }

    private void validateActivationDate(final List<ApiParameterError> dataValidationErrors) {

        if (getSubmittedOnDate() != null && isDateInTheFuture(getSubmittedOnDate())) {

            final String defaultUserMessage = "submitted date cannot be in the future.";
            final ApiParameterError error = ApiParameterError.parameterError("error.msg.clients.submittedOnDate.in.the.future",
                    defaultUserMessage, ClientApiConstants.submittedOnDateParamName, this.submittedOnDate);

            dataValidationErrors.add(error);
        }

        if (getActivationLocalDate() != null && getSubmittedOnDate() != null && getSubmittedOnDate().isAfter(getActivationLocalDate())) {

            final String defaultUserMessage = "submitted date cannot be after the activation date";
            final ApiParameterError error = ApiParameterError.parameterError("error.msg.clients.submittedOnDate.after.activation.date",
                    defaultUserMessage, ClientApiConstants.submittedOnDateParamName, this.submittedOnDate);

            dataValidationErrors.add(error);
        }

		if (getReopenedDate() != null && getActivationLocalDate() != null
				&& getReopenedDate().isAfter(getActivationLocalDate())) {

			final String defaultUserMessage = "reopened date cannot be after the submittedon date";
			final ApiParameterError error = ApiParameterError.parameterError(
					"error.msg.clients.submittedOnDate.after.reopened.date", defaultUserMessage,
					ClientApiConstants.reopenedDateParamName, this.reopenedDate);

			dataValidationErrors.add(error);
		}

        if (getActivationLocalDate() != null && isDateInTheFuture(getActivationLocalDate())) {

            final String defaultUserMessage = "Activation date cannot be in the future.";
            final ApiParameterError error = ApiParameterError.parameterError("error.msg.clients.activationDate.in.the.future",
                    defaultUserMessage, ClientApiConstants.activationDateParamName, getActivationLocalDate());

            dataValidationErrors.add(error);
        }
    }

    private void deriveDisplayName() {

        StringBuilder nameBuilder = new StringBuilder();
        Integer legalForm = this.getLegalForm();
        if(legalForm == null || LegalForm.fromInt(legalForm).isPerson())
        {
        	if (StringUtils.isNotBlank(this.firstname)) {
                nameBuilder.append(this.firstname).append(' ');
            }

            if (StringUtils.isNotBlank(this.middlename)) {
                nameBuilder.append(this.middlename).append(' ');
            }

            if (StringUtils.isNotBlank(this.lastname)) {
                nameBuilder.append(this.lastname);
            }
            
            if (StringUtils.isNotBlank(this.fullname)) {
                nameBuilder = new StringBuilder(this.fullname);
            }
        }
        else if(LegalForm.fromInt(legalForm).isEntity())
        {
        	if (StringUtils.isNotBlank(this.fullname)) {
                nameBuilder = new StringBuilder(this.fullname);
            }
        }
        
        this.displayName = nameBuilder.toString();
    }

    public LocalDate getSubmittedOnDate() {
        return (LocalDate) ObjectUtils.defaultIfNull(new LocalDate(this.submittedOnDate), null);
    }

    public LocalDate getActivationLocalDate() {
        LocalDate activationLocalDate = null;
        if (this.activationDate != null) {
            activationLocalDate = LocalDate.fromDateFields(this.activationDate);
        }
        return activationLocalDate;
    }

    public LocalDate getOfficeJoiningLocalDate() {
        LocalDate officeJoiningLocalDate = null;
        if (this.officeJoiningDate != null) {
            officeJoiningLocalDate = LocalDate.fromDateFields(this.officeJoiningDate);
        }
        return officeJoiningLocalDate;
    }

    public String mobileNo() {
        return this.mobileNo;
    }

	public String emailAddress() {
        return this.emailAddress;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

	public boolean isNotStaff() {
        return !isStaff();
    }

    public boolean isStaff() {
        return this.isStaff;
    }

	public String getExternalId() {
		return this.externalId; 
	}

	public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public void updateOfficeJoiningDate(final Date date) {
        this.officeJoiningDate = date;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public boolean isActivatedAfter(final LocalDate submittedOn) {
        return getActivationLocalDate().isAfter(submittedOn);
    }


    public Long savingsProductId() {
        return this.savingsProductId;
    }

    public void updateSavingsProduct(final Long savingsProductId) {
        this.savingsProductId = savingsProductId;
    }

    public AppUser activatedBy() {
        return this.activatedBy;
    }

    public Long savingsAccountId() {
        return this.savingsAccountId;
    }

    public void updateSavingsAccount(Long savingsAccountId) {
        this.savingsAccountId = savingsAccountId;
    }

    public LocalDate getClosureDate() {
        return (LocalDate) ObjectUtils.defaultIfNull(new LocalDate(this.closureDate), null);
    }
    public LocalDate getRejectedDate() {
        return (LocalDate) ObjectUtils.defaultIfNull(new LocalDate(this.rejectionDate), null);
    }
    public LocalDate getWithdrawalDate() {
        return (LocalDate) ObjectUtils.defaultIfNull(new LocalDate(this.withdrawalDate), null);
	}

	public LocalDate getReopenedDate() {
		return this.reopenedDate == null ? null : new LocalDate(this.reopenedDate);
	}

    public Date dateOfBirth() {
        return this.dateOfBirth;
    }

    public LocalDate dateOfBirthLocalDate() {
        LocalDate dateOfBirth = null;
        if (this.dateOfBirth != null) {
            dateOfBirth = LocalDate.fromDateFields(this.dateOfBirth);
        }
        return dateOfBirth;
    }

    public void reject(AppUser currentUser, Date rejectionDate) {
        this.rejectionDate = rejectionDate;
        this.rejectedBy = currentUser;
        this.updatedBy = currentUser;
        this.updatedOnDate = rejectionDate;
        this.status = ClientStatus.REJECTED.getValue();

    }

    public void withdraw(AppUser currentUser, Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
        this.withdrawnBy = currentUser;
        this.updatedBy = currentUser;
        this.updatedOnDate = withdrawalDate;
        this.status = ClientStatus.WITHDRAWN.getValue();

    }

    public void reActivate(AppUser currentUser, Date reactivateDate) {
        this.closureDate = null;
        this.reactivateDate = reactivateDate;
        this.reactivatedBy = currentUser;
        this.updatedBy = currentUser;
        this.updatedOnDate = reactivateDate;
        this.status = ClientStatus.PENDING.getValue();

    }
    
    public void reOpened(AppUser currentUser, Date reopenedDate) {
        this.reopenedDate = reopenedDate;
        this.reopenedBy = currentUser;
        this.updatedBy = currentUser;
        this.updatedOnDate = reopenedDate;
        this.status = ClientStatus.PENDING.getValue();

    }

    public Integer getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(Integer legalForm) {
        this.legalForm = legalForm;
    }
    
    public String getFirstname(){return this.firstname;}

    public String getMiddlename(){return this.middlename;}

    public String getLastname(){return this.lastname;}
}