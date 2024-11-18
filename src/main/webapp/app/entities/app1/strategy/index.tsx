import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Strategy from './strategy';
import StrategyDetail from './strategy-detail';
import StrategyUpdate from './strategy-update';
import StrategyDeleteDialog from './strategy-delete-dialog';

const StrategyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Strategy />} />
    <Route path="new" element={<StrategyUpdate />} />
    <Route path=":id">
      <Route index element={<StrategyDetail />} />
      <Route path="edit" element={<StrategyUpdate />} />
      <Route path="delete" element={<StrategyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StrategyRoutes;
