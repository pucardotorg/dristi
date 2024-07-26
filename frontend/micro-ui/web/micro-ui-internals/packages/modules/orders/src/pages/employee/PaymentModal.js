import React, { useState } from 'react';
import { Modal, Button, CardText, FormComposerV2 ,RadioButtons, CardLabel} from '@egovernments/digit-ui-react-components';
import { useTranslation } from 'react-i18next';

const PaymentForSummonModal = ({ onClose, onPayOnline }) => {
  const { t } = useTranslation();
  const [selectedOption, setSelectedOption] = useState('e-post');

  const feeOptions = {
    'e-post': [
      { label: 'Court Fees', amount: '₹5/-', action: 'Pay Online', onClick: () => onPayOnline('courtFees') },
      { label: 'Delivery Partner Fee', amount: '₹520/-', action: 'Pay Online', onClick: () => onPayOnline('deliveryFee') },
    ],
    'registered-post': [
      { label: 'Court Fees', amount: '₹5/-', action: 'Pay Online', onClick: () => onPayOnline('courtFees') },
      { label: 'Delivery Partner Fee', amount: '₹120/-', action: 'Pay Online', onClick: () => onPayOnline('deliveryFee') },
    ],
  };

  return (
    <Modal
      popupStyles={{
        height: "auto",
        maxHeight: "700px",
        width: "700px",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        padding: "20px"
      }}
      headerBarMain={<h1 className='heading-m'>{t("Payment for Summon via Post")}</h1>}
      actionSaveLabel="Close"
      actionSaveOnSubmit={onClose}
    >
      <CardText>
        <div>
          <p>
            {t("It takes 10-15 days via physical post and 3-5 days via e-post for Summon Delivery. Pay by 23 Jun 2024 for on-time delivery before next hearing.")}
          </p>
          <p><strong>{t("Issued to")}:</strong> Vikram Singh</p>
          <p><strong>{t("Next Hearing Date")}:</strong> 04/07/2024</p>
          <p><strong>{t("Delivery Channel")}:</strong> Post (227, 5th Cross, 13th Main, Indiranagar Bengaluru, Karnataka, 560068)</p>
        </div>
        <div style={{ margin: "20px 0" }}>
            <CardLabel>{t("Select preferred mode of post to pay")}</CardLabel>
            <RadioButtons
              options={[t('e-post'), t('registered-post')]}
              selectedOption={selectedOption}
              onSelect={(value) => setSelectedOption(value)}
            />
          </div>
        <div style={{ margin: "20px 0" }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #ccc', paddingBottom: '10px' }}>
            <span><strong>{t("Fee Type")}</strong></span>
            <span><strong>{t("Amount")}</strong></span>
            <span><strong>{t("Actions")}</strong></span>
          </div>
          {feeOptions[selectedOption].map((option, index) => (
            <div key={index} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', margin: '10px 0' }}>
              <span>{t(option.label)}</span>
              <span>{option.amount}</span>
              <Button label={t(option.action)} onClick={option.onClick} />
            </div>
          ))}
        </div>
      </CardText>
    </Modal>
  );
};

export default PaymentForSummonModal;
