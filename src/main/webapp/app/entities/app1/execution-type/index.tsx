import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExecutionType from './execution-type';
import ExecutionTypeDetail from './execution-type-detail';
import ExecutionTypeUpdate from './execution-type-update';
import ExecutionTypeDeleteDialog from './execution-type-delete-dialog';

const ExecutionTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExecutionType />} />
    <Route path="new" element={<ExecutionTypeUpdate />} />
    <Route path=":id">
      <Route index element={<ExecutionTypeDetail />} />
      <Route path="edit" element={<ExecutionTypeUpdate />} />
      <Route path="delete" element={<ExecutionTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExecutionTypeRoutes;
