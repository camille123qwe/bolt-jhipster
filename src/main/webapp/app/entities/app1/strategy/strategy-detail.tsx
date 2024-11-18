import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './strategy.reducer';

export const StrategyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const strategyEntity = useAppSelector(state => state.app1.strategy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="strategyDetailsHeading">
          <Translate contentKey="app1App.app1Strategy.detail.title">Strategy</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="app1App.app1Strategy.id">Id</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="app1App.app1Strategy.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.uuid}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="app1App.app1Strategy.label">Label</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="app1App.app1Strategy.description">Description</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="app1App.app1Strategy.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.createdAt ? <TextFormat value={strategyEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="executionRule">
              <Translate contentKey="app1App.app1Strategy.executionRule">Execution Rule</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.executionRule}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="app1App.app1Strategy.status">Status</Translate>
            </span>
          </dt>
          <dd>{strategyEntity.status}</dd>
          <dt>
            <Translate contentKey="app1App.app1Strategy.executionType">Execution Type</Translate>
          </dt>
          <dd>{strategyEntity.executionType ? strategyEntity.executionType.id : ''}</dd>
          <dt>
            <Translate contentKey="app1App.app1Strategy.source">Source</Translate>
          </dt>
          <dd>{strategyEntity.source ? strategyEntity.source.id : ''}</dd>
          <dt>
            <Translate contentKey="app1App.app1Strategy.project">Project</Translate>
          </dt>
          <dd>{strategyEntity.project ? strategyEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/app1/strategy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app1/strategy/${strategyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StrategyDetail;
