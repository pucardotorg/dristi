CREATE INDEX IF NOT EXISTS idx_dristi_order_tenant_id ON dristi_orders (tenantId);
CREATE INDEX IF NOT EXISTS idx_dristi_order_application_number ON dristi_orders USING GIN (applicationNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_order_order_number ON dristi_orders (orderNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_order_filing_number ON dristi_orders (filingNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_order_cnr_number ON dristi_orders (cnrNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_order_order_type ON dristi_orders (orderType);
CREATE INDEX IF NOT EXISTS idx_dristi_order_status ON dristi_orders (status);

CREATE INDEX IF NOT EXISTS idx_dristi_order_document_order_id ON dristi_order_document (order_id);

CREATE INDEX IF NOT EXISTS idx_dristi_order_statute_section_order_id ON dristi_order_statute_section (order_id);