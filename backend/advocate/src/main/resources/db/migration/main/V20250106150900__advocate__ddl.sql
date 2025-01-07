CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_dristi_advocate_tenant_id ON dristi_advocate(tenantId);

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_dristi_advocate_clerk_tenant_id ON dristi_advocate_clerk(tenantId);

